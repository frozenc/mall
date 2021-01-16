package com.cc.mall.app.component.mq;

import com.cc.mall.app.facade.OrderMasterFacade;
import com.cc.mall.common.base.GlobalException;
import com.cc.mall.common.utils.api.ResultCode;
import com.cc.mall.common.utils.enums.OrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/14 18:53
 **/
@Slf4j
@Component
@Transactional
@RocketMQMessageListener(consumerGroup = DelayMessage.TOPIC + "-group", topic = DelayMessage.TOPIC)
public class DelayConsumer implements RocketMQListener<DelayMessage> {
    @Autowired
    private OrderMasterFacade orderMasterFacade;

    @Override
    public void onMessage(DelayMessage delayMessage) {
        Long id = delayMessage.getOrderId();
        try {
            if (orderMasterFacade.get(id).getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) {
                //订单超时未付款，取消
                orderMasterFacade.returnStock(id);
                orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.CANCEL.getCode());
                log.info("取消订单成功, 订单ID:" + id);
            } else {
                log.info("订单已付款, 订单ID:" + id);
            }
        } catch (Exception e) {
            log.info("取消订单失败, 订单ID:" + id);
            throw new GlobalException(ResultCode.FAILED);
        }
    }
}

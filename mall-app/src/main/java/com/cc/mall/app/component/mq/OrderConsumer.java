package com.cc.mall.app.component.mq;

import cn.hutool.json.JSONUtil;
import com.cc.mall.app.dao.StockDao;
import com.cc.mall.common.base.GlobalException;
import com.cc.mall.common.utils.api.ResultCode;
import com.cc.mall.mbg.entity.OrderDetail;
import com.cc.mall.mbg.entity.OrderMaster;
import com.cc.mall.mbg.entity.Product;
import com.cc.mall.mbg.entity.User;
import com.cc.mall.mbg.service.OrderDetailService;
import com.cc.mall.mbg.service.OrderMasterService;
import com.cc.mall.mbg.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/14 16:57
 **/
@Slf4j
@Component
@Transactional
@RocketMQMessageListener(consumerGroup = OrderMessage.TOPIC + "-group", topic = OrderMessage.TOPIC)
public class OrderConsumer implements RocketMQListener<OrderMessage> {
    @Autowired
    private StockDao stockDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    //不会超卖
    @Override
    public void onMessage(OrderMessage orderMessage) {
        Long orderId = orderMessage.getOrderId();
        try {
            User user = orderMessage.getUser();
            Map<Long, Integer> cart = orderMessage.getCart();

            StringBuilder sb = new StringBuilder();
            sb.append("订单号：" + orderId).append("\n");
            //计算价格
            BigDecimal amount = new BigDecimal(0);
            for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
                Long productId = entry.getKey();
                Integer productQuantity = entry.getValue();
                stockDao.subStock(productId, productQuantity);
                //累加价格
                Product product = productService.getById(productId);
                amount = amount.add(product.getPrice().multiply(new BigDecimal(productQuantity)));
                //创建订单详情
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(orderId);
                orderDetail.setProductId(productId);
                orderDetail.setProductName(product.getName());
                orderDetail.setProductQuantity(productQuantity);
                orderDetail.setProductIcon(product.getIcon());
                orderDetail.setProductPrice(product.getPrice());
                orderDetailService.save(orderDetail);
                sb.append(JSONUtil.toJsonStr(orderDetail)).append("\n");
            }
            sb.append("总价：").append(amount).append("\n");
            //创建订单
            OrderMaster orderMaster = new OrderMaster();
            orderMaster.setId(orderId);
            orderMaster.setAmount(amount);
            orderMaster.setUsername(user.getUsername());
             orderMaster.setEmail(user.getEmail());
            orderMaster.setNickname(user.getNickname());
            orderMasterService.save(orderMaster);

            //todo 发送邮件通知

            //超时取消
            rocketMQTemplate.syncSend(DelayMessage.TOPIC, MessageBuilder.withPayload(orderMessage).build(), TIME_OUT, ONE_MIN);
            log.info("订单创建成功, 订单ID:" + orderId);
        } catch (Exception e) {
            log.info("订单创建失败, 订单ID:" + orderId);
            throw new GlobalException(ResultCode.FAILED); //RocketMQ自动重试16次
        }
    }

    /**
     * RocketMQ 不支持任意时间自定义的延迟消息，仅支持内置预设值的延迟时间间隔的延迟消息。
     * 预设值的延迟时间间隔为：1s、 5s、 10s、 30s、 1m、 2m、 3m、 4m、 5m、 6m、 7m、 8m、 9m、 10m、 20m、 30m、 1h、 2h
     */
    //发送消息的等待时间
    private static final long TIME_OUT = 30 * 1000; //30s
    private static final int ONE_MIN = 5;
    private static final int FIVE_MIN = 9;
    private static final int TEN_MIN = 14;
}

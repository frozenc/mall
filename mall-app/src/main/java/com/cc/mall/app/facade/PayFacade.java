package com.cc.mall.app.facade;

import com.cc.mall.common.base.GlobalException;
import com.cc.mall.common.component.alipay.AlipayService;
import com.cc.mall.common.utils.api.ResultCode;
import com.cc.mall.common.utils.enums.OrderStatusEnum;
import com.cc.mall.mbg.entity.OrderMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/16 11:33
 **/
@Service
@Transactional
public class PayFacade {
    @Autowired
    private AlipayService alipayService;

    @Autowired
    private OrderMasterFacade orderMasterFacade;

    //付款
    public void pay(String username, Long id, HttpServletResponse response) {
        //确认订单存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        alipayService.pay(orderMaster.getId(), orderMaster.getAmount(), response);
    }

    //订单关闭
    public void close(String username, Long id) {
        //确认订单存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        //确认订单处于待付款状态才能关闭
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) {
            throw new GlobalException(ResultCode.ORDER_NOT_TO_BE_PAID);
        }
        //增加库存
        orderMasterFacade.returnStock(id);
        //用户关闭订单
        orderMasterFacade.updateOrderStatus(orderMaster.getId(), OrderStatusEnum.USER_CLOSE.getCode());
        alipayService.close(orderMaster.getId());
    }

    //确认收货
    public void reveive(String username, Long id) {
        //确认订单存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        //确认订单处于待收货状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_RECEIVED.getCode())) {
            throw new GlobalException(ResultCode.ORDER_NOT_TO_BE_RECEIVED);
        }
        //订单完成
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.FINISH.getCode());
    }

    //订单处理退款
    public void refund(String username, Long id) {
        //确认订单存在
        OrderMaster orderMaster = orderMasterFacade.get(username, id);
        //确认订单处于待发货状态
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) {
            throw new GlobalException(ResultCode.ORDER_NOT_TO_BE_SHIPPED);
        }
        //退款
        orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.REFUND_REQUEST.getCode());
        alipayService.refund(orderMaster.getId(), orderMaster.getAmount());
    }
}

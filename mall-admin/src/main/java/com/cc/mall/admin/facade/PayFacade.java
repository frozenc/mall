package com.cc.mall.admin.facade;

import com.cc.mall.common.base.GlobalException;
import com.cc.mall.common.component.pay.AlipayService;
import com.cc.mall.common.utils.api.ResultCode;
import com.cc.mall.common.utils.enums.OrderStatusEnum;
import com.cc.mall.mbg.entity.OrderMaster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/16 11:33
 **/
@Service
public class PayFacade {
    @Autowired
    private AlipayService alipayService;

    @Autowired
    private OrderMasterFacade orderMasterFacade;

    //订单处理退款
    public void refund(Long id) {
        //确认订单存在
        OrderMaster orderMaster = orderMasterFacade.get(id);
        //检查订单是否处于退款申请
        if (!orderMaster.getStatus().equals(OrderStatusEnum.REFUND_REQUEST.getCode())) {
            throw new GlobalException(ResultCode.ORDER_NOT_REFUND_REQUEST);
        }
        boolean isSuccess = alipayService.refund(orderMaster.getId(), orderMaster.getAmount());
        if (isSuccess) {
            //退款成功
            orderMasterFacade.returnStock(orderMaster.getId());
            orderMasterFacade.updateOrderStatus(orderMaster.getId(), OrderStatusEnum.REFUND_SUCCESS.getCode());
        } else {
            throw new GlobalException(ResultCode.REFUND_BUG);
        }
    }

    //订单关闭
    public void close(Long id) {
        //确认订单存在
        OrderMaster orderMaster = orderMasterFacade.get(id);
        //检查订单是否处于待付款
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_PAID.getCode())) {
            throw new GlobalException(ResultCode.ORDER_NOT_TO_BE_PAID);
        }
        //关闭订单
        orderMasterFacade.returnStock(orderMaster.getId());
        orderMasterFacade.updateOrderStatus(orderMaster.getId(), OrderStatusEnum.GM_CLOSE.getCode());
    }

    //订单发货
    public void ship(Long id) {
        //确认订单存在
        OrderMaster orderMaster = orderMasterFacade.get(id);
        //检查订单是否处于待发货
        if (!orderMaster.getStatus().equals(OrderStatusEnum.TO_BE_SHIPPED.getCode())) {
            throw new GlobalException(ResultCode.ORDER_NOT_TO_BE_SHIPPED);
        }
        //订单处于待收货
        orderMasterFacade.updateOrderStatus(orderMaster.getId(), OrderStatusEnum.TO_BE_RECEIVED.getCode());
    }
}

package com.cc.mall.app.component.mq;

import lombok.Data;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/13 21:48
 **/
@Data
public class DelayMessage {
    //延迟消息取消订单
    public static final String TOPIC = "DELAY_CANCEL_ORDER";

    private Long orderId;
}

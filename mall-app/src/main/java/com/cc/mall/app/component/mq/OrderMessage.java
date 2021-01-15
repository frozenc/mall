package com.cc.mall.app.component.mq;

import com.cc.mall.mbg.entity.User;
import lombok.Data;

import java.util.Map;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/13 21:47
 **/
@Data
public class OrderMessage {
    //创建订单
    public static final String TOPIC = "CREATE_ORDER";

    private Long orderId;

    private User user;

    private Map<Long, Integer> cart;
}

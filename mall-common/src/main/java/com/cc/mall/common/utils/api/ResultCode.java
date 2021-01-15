package com.cc.mall.common.utils.api;

/**
 * 枚举了一些常用API操作码
 * Created by macro on 2019/4/19.
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),


    //服务器
    BIND_EXCEPTION(1000, "参数错误"),

    //商品相关
    CART_EMPTY(50000, "购物车为空"),
    PRODUCT_NOT_EXIST(50010, "商品不存在"),
    PRODUCT_STOCK_NOT_ENOUGH(50011, "商品库存不足"),

    //订单相关
    ORDER_NOT_EXIST(60000, "订单不存在"),
    ORDER_NOT_TO_BE_PAID(60001, "订单不属于待付款"),
    ORDER_NOT_TO_BE_SHIPPED(60002, "订单不属于待发货"),
    ORDER_NOT_TO_BE_RECEIVED(60003, "订单不属于待收货"),
    ORDER_NOT_REFUND_REQUEST(60004, "订单没有退款申请"),
    ;

    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

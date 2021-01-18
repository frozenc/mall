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

    // 验证码
    CAPTCHA_EXPIRE(20000, "验证码过期"),
    CAPTCHA_ERROR(20001, "验证码错误"),

    // 上传文件
    FILE_UPLOAD_EMPTY(30000, "上传文件不能为空"),
    FILE_UPLOAD_ERROR(30001, "上传文件出错，debug中..."),

    //订单相关
    ORDER_NOT_EXIST(60000, "订单不存在"),
    ORDER_NOT_TO_BE_PAID(60001, "订单不属于待付款"),
    ORDER_NOT_TO_BE_SHIPPED(60002, "订单不属于待发货"),
    ORDER_NOT_TO_BE_RECEIVED(60003, "订单不属于待收货"),
    ORDER_NOT_REFUND_REQUEST(60004, "订单没有退款申请"),

    // 支付相关
    PAY_BUG(70000, "支付异常"),
    PAY_CHECK_BUG(70001, "支付检查异常"),
    REFUND_BUG(70002, "订单退款异常"),
    CLOSE_BUG(70003, "订单关闭异常"),
    CANCEL_BUG(70004, "订单取消异常"),
    QUERY_BUG(70005, "订单查询异常");
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

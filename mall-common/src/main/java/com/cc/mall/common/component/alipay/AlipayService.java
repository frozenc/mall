package com.cc.mall.common.component.alipay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/16 10:39
 **/
public interface AlipayService {

    /**
     * 支付 https://opendocs.alipay.com/apis/api_1/alipay.trade.page.pay
     */
    public void pay(Long id, BigDecimal amount, HttpServletResponse response);

    /**
     * 退款 https://opendocs.alipay.com/apis/api_1/alipay.trade.refund
     */
    public boolean refund(Long id, BigDecimal amount);

    /**
     * 用户超时未支付关闭订单 https://opendocs.alipay.com/apis/api_1/alipay.trade.close
     */
    public boolean close(Long id);

    /**
     * 支付交易返回失败需撤销交易 https://opendocs.alipay.com/apis/api_1/alipay.trade.cancel
     */
    public boolean cancel(Long id);

    /**
     * 查询订单结果 https://opendocs.alipay.com/apis/api_1/alipay.trade.query
     */
    public boolean query(Long id);

    /**
     * 检查密钥
     */
    public boolean check(HttpServletRequest request);
}

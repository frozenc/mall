package com.cc.mall.app.controller;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.cc.mall.app.facade.OrderMasterFacade;
import com.cc.mall.common.component.pay.AlipayService;
import com.cc.mall.common.utils.enums.OrderStatusEnum;
import com.cc.mall.common.utils.utils.Constants;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/16 16:01
 **/
@Slf4j
@RestController
@Api("支付宝支付回调")
public class AlipayController {
    @Autowired
    private OrderMasterFacade orderMasterFacade;

    @Autowired
    private AlipayService alipayService;

    /**
     * 支付宝确认支付同步通知接口,客户端接收
     */
    @RequestMapping("/return")
    @ResponseBody
    public String Return(HttpServletRequest request) {
        boolean signVerified = alipayService.check(request);
        if(signVerified){
            // 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            log.info("Return 验证成功");
            return Constants.PAY_SUCCESS_RETURN;
        }else{
            // 验签失败则记录异常日志，并在response中返回failure.
            log.info("Return 验证失败");
            return Constants.PAY_FAILURE_RETURN;
        }
    }

    /**
     * 支付宝交易成功异步通知接口
     */
    @RequestMapping("/notify")
    @ResponseBody
    public String Notify(HttpServletRequest request) {
        boolean signVerified = alipayService.check(request);
        if(signVerified){
            // 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            log.info("Notify 验证成功");
            Long id = new Long(request.getParameter("out_trade_no"));
            orderMasterFacade.updateOrderStatus(id, OrderStatusEnum.TO_BE_SHIPPED.getCode());
            return "success";
        }else{
            // 验签失败则记录异常日志，并在response中返回failure.
            log.info("Notify 验证失败");
            return "failure";
        }
    }

}

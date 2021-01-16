package com.cc.mall.common.component.pay;

import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.cc.mall.common.base.GlobalException;
import com.cc.mall.common.utils.api.ResultCode;
import com.cc.mall.common.utils.utils.Constants;
import com.cc.mall.common.utils.utils.RenderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/16 10:38
 **/
@Service
@Slf4j
public class AlipayServiceImpl implements AlipayService{
    @Autowired
    private AlipayConfig alipayConfig;

    @Autowired
    private AlipayClient alipayClient;

    @Override
    public void pay(Long id, BigDecimal amount, HttpServletResponse response) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //支付成功回调
        request.setReturnUrl(alipayConfig.getRETURN_URL());
        //交易完成回调
        request.setNotifyUrl(alipayConfig.getNOTIFY_URL());
        Map<String, Object> biz = new HashMap<>();
        biz.put("out_trade_no", id);
        biz.put("product_code", Constants.PAY_PRODUCT_CODE);
        biz.put("total_amount", amount);
        biz.put("subject", Constants.PAY_SUBJECT);
        biz.put("body", Constants.PAY_BODY);
        log.info(JSONUtil.toJsonStr(biz));
        request.setBizContent(JSONUtil.toJsonStr(biz));
        String form= null;  //调用SDK生成表单
        try {
            form = alipayClient.pageExecute(request).getBody();
            log.info(alipayClient.pageExecute(request, "GET").getBody());
            RenderUtil.render(response, form);
        } catch (AlipayApiException e) {
            throw new GlobalException(ResultCode.PAY_BUG);
        } catch (IOException e) {
            throw new GlobalException(ResultCode.FAILED);
        }
    }

    @Override
    public boolean refund(Long id, BigDecimal amount) {
        Map<String, Object> params = new HashMap<>();
        params.put("out_trade_no", id);
        params.put("refund_amount", amount); // 必选

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(JSONUtil.toJsonStr(params));
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
            return response.isSuccess();
        } catch (AlipayApiException e) {
            throw new GlobalException(ResultCode.REFUND_BUG);
        }
    }

    @Override
    public boolean close(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("out_trade_no", id);

        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        request.setBizContent(JSONUtil.toJsonStr(params));
        try {
            AlipayTradeCloseResponse response = alipayClient.execute(request);
            return response.isSuccess();
        } catch (AlipayApiException e) {
            throw new GlobalException(ResultCode.CLOSE_BUG);
        }
    }

    @Override
    public boolean cancel(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("out_trade_no", id);

        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.setBizContent(JSONUtil.toJsonStr(params));
        try {
            AlipayTradeCancelResponse response = alipayClient.execute(request);
            return response.isSuccess();
        } catch (AlipayApiException e) {
            throw new GlobalException(ResultCode.CANCEL_BUG);
        }
    }

    @Override
    public boolean query(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("out_trade_no", id);

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent(JSONUtil.toJsonStr(params));
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            // code为10000说明接口调用成功，订单成功查询
            return response.getCode().equals("10000");
        } catch (AlipayApiException e) {
            throw new GlobalException(ResultCode.QUERY_BUG);
        }
    }

    @Override
    public boolean check(HttpServletRequest request) {
        Map<String, String> paramsMap = requestToMap(request); //将异步通知中收到的所有参数都存放到map中
        log.info(JSONUtil.toJsonStr(paramsMap));
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, alipayConfig.getALIPAY_PUBLIC_KEY(),
                    alipayConfig.getCHARSET(), alipayConfig.getSIGN_TYPE()); //调用SDK验证签名
            return signVerified;
        } catch (AlipayApiException e) {
            throw new GlobalException(ResultCode.PAY_CHECK_BUG);
        }
    }

    private static Map<String, String> requestToMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        Iterator iterator = requestParams.keySet().iterator();
        while(iterator.hasNext()) {
            String name = (String) iterator.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }
}

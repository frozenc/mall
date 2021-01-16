package com.cc.mall.common.component.pay;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * mall-app
 * 支付宝沙箱接口配置
 *
 * @author Chan
 * @since 2021/1/16 10:38
 **/
@Data
@Configuration
@PropertySource("classpath:alipay.properties")
public class AlipayConfig {

    @Value("${alipay.appID}")
    private String APP_ID;
    @Value("${alipay.gateway}")
    private String GATEWAY;
    @Value("${alipay.publicKey}")
    private String ALIPAY_PUBLIC_KEY;
    @Value("${alipay.secretKey}")
    private String APP_SECRET_KEY;
    @Value("${alipay.authUrl}")
    private String AUTH_URL;
    @Value("${alipay.notifyUrl}")
    private String NOTIFY_URL;
    @Value("${alipay.returnUrl}")
    private String RETURN_URL;

    private final String FORMAT = "json";
    private final String CHARSET = "UTF-8";
    private final String SIGN_TYPE = "RSA2";

    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(GATEWAY, APP_ID, APP_SECRET_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY, SIGN_TYPE);
    }
}

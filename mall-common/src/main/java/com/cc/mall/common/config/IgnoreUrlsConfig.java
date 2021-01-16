package com.cc.mall.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@PropertySource({"classpath:filter.properties"})
@ConfigurationProperties(prefix = "ignore")
@Component
public class IgnoreUrlsConfig {

    private List<String> urls = new ArrayList<>();
}

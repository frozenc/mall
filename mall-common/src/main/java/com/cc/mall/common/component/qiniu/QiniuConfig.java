package com.cc.mall.common.component.qiniu;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/18 15:01
 **/
@Configuration
@PropertySource("classpath:qiniu.properties")
@Getter
public class QiniuConfig {

    @Value("${qiniu.access.key}")
    private String accessKey;

    @Value("${qiniu.secret.key}")
    private String secretKey;

    @Value("${qiniu.bucket.name}")
    private String bucketName;

    @Value("${qiniu.domain.name}")
    private String domainName;

    private com.qiniu.storage.Configuration getConfiguration() {
        return new com.qiniu.storage.Configuration(Region.huanan());
    }

    @Bean
    public UploadManager getUploadManager() {
        return new UploadManager(getConfiguration());
    }

    @Bean
    public Auth getAuth() {
        return Auth.create(accessKey, secretKey);
    }

    @Bean
    public BucketManager getBucketManager() {
        return new BucketManager(getAuth(), getConfiguration());
    }

}

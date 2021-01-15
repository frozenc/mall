package com.cc.mall.app;

import com.cc.mall.common.component.redis.service.RedisService;
import com.cc.mall.common.utils.utils.Constants;
import com.cc.mall.mbg.entity.Product;
import com.cc.mall.mbg.service.ProductService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@MapperScan(basePackages = {"com.cc.mall.mbg.mapper", "com.cc.mall.common.dao", "com.cc.mall.app.dao"})
@ComponentScan(basePackages = {"com.cc.mall.mbg", "com.cc.mall.app", "com.cc.mall.common"})
@EnableScheduling
public class MallAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MallAppApplication.class, args);
    }

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        List<Product> productList = productService.list();
        Map<String, Object> stockMap = new HashMap<>();
        productList.forEach(product -> {
            stockMap.put(Constants.PRODUCT_STOCK + product.getId(), product.getStock());
        });
        redisService.multiSet(stockMap);
    }
}

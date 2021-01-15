package com.cc.mall.mbg;


import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ResourceBundle;

/**
 * cc_mall
 * Mybatis-Plus代码生成器
 * MyBatis-Plus Generator https://mp.baomidou.com/guide/config.html
 * @author Chan
 * @since 2021/1/7 11:24
 **/
public class MbgGenerator {
    private static final String MBG = "/mall-mbg";

    public static void main(String[] args) {
        final ResourceBundle rb = ResourceBundle.getBundle("application");

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir") + MBG;
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("cc");
        gc.setOpen(false);
        gc.setFileOverride(true);
        gc.setServiceName("%sService"); // service命名方式
        gc.setServiceImplName("%sServiceImpl");
        gc.setDateType(DateType.ONLY_DATE);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(rb.getString("spring.datasource.url"));
        dsc.setDriverName(rb.getString("spring.datasource.driver-class-name"));
        dsc.setUsername(rb.getString("spring.datasource.username"));
        dsc.setPassword(rb.getString("spring.datasource.password"));
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com.cc.mall.mbg"); // 生成的目录
        mpg.setPackageInfo(pc);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        templateConfig.setController(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        mpg.setStrategy(strategy);

        // 设置模板引擎
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        mpg.execute();
    }
}

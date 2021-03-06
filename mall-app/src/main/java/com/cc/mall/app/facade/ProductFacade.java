package com.cc.mall.app.facade;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.mall.common.utils.dto.ProductDto;
import com.cc.mall.common.utils.dto.page.ProductPageRequest;
import com.cc.mall.mbg.entity.Product;
import com.cc.mall.mbg.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * PmsBrandService实现类
 * Created by macro on 2019/4/19.
 */
@Transactional
@Service
public class ProductFacade{

    @Autowired
    private ProductService productService;

    public Page<Product> listProducts(ProductPageRequest pageRequest) {
        Page<Product> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(pageRequest.getKeyword())) {
            //第一个参数传列名，通过mapper的get方法也可以
            queryWrapper.lambda().like(Product::getName, pageRequest.getKeyword());
        }
        if (!StringUtils.isEmpty(pageRequest.getCategory())) {
            queryWrapper.lambda().eq(Product::getCategory, pageRequest.getCategory());
        }
        queryWrapper.lambda().eq(Product::getStatus, true);
        queryWrapper.lambda().orderByDesc(Product::getId);
        return productService.page(page, queryWrapper);
    }
}

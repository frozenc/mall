package com.cc.mall.admin.facade;

import cn.hutool.core.convert.Convert;
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
            queryWrapper.lambda().like(Product::getCategory, pageRequest.getCategory());
        }
        queryWrapper.lambda().orderByDesc(Product::getId);
        return productService.page(page, queryWrapper);
    }

    public boolean createProduct(ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto);
        return productService.save(product);
    }

    public boolean updateProduct(Long id, ProductDto productDto) {
        Product product = Convert.convert(Product.class, productDto);
        product.setId(id);
        return productService.updateById(product);
    }

    public boolean deleteProduct(Long id) {
        return productService.removeById(id);
    }

    public Product getProduct(Long id) {
        return productService.getById(id);
    }
}

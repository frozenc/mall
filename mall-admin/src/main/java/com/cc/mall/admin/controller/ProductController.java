package com.cc.mall.admin.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.api.PageResult;
import com.cc.mall.common.utils.dto.ProductDto;
import com.cc.mall.common.utils.dto.page.ProductPageRequest;
import com.cc.mall.common.utils.vo.ProductVo;
import com.cc.mall.admin.facade.ProductFacade;
import com.cc.mall.mbg.entity.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 品牌管理Controller
 * Created by cc on 2019/4/19.
 */
@Api(tags = "商品管理")
@RestController
@Slf4j
@RequestMapping("/product")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ProductController {
    @Autowired
    private ProductFacade productFacade;


    @ApiOperation("获取所有商品列表")
    @GetMapping(value = "/listAll")
    @ResponseBody
    public PageResult<List<ProductVo>> getBrandList(@Valid ProductPageRequest pageRequest) {
        Page<Product> page = productFacade.listProducts(pageRequest);
        List<ProductVo> productVos = Convert.convert(new TypeReference<List<ProductVo>>() {
        }, page.getRecords());
        return PageResult.success(productVos, page.getTotal());
    }

    @ApiOperation("添加商品")
    @PostMapping(value = "")
    @ResponseBody
    public CommonResult<String> createProduct(@Valid ProductDto productDto) {
        boolean result = productFacade.createProduct(productDto);
        CommonResult commonResult;
        if (result) {
            commonResult = CommonResult.success(productDto.getName());
            log.debug("createBrand success:{}", productDto.getName());
        } else {
            commonResult = CommonResult.failed("操作失败");
            log.debug("createBrand failed:{}", productDto.getName());
        }
        return commonResult;
    }

    @ApiOperation("更新指定id商品")
    @PutMapping(value = "/{id}")
    @ResponseBody
    public CommonResult<String> updateProduct(@PathVariable("id") Long id, @Valid ProductDto productDto) {
        boolean result = productFacade.updateProduct(id, productDto);
        if (result) {
            log.debug("updateBrand success:{}", productDto.getName());
            return CommonResult.success(productDto.getName());
        } else {
            log.debug("updateBrand failed:{}", productDto.getName());
            return CommonResult.failed("操作失败");
        }
    }

    @ApiOperation("删除指定id商品")
    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public CommonResult<String> deleteBrand(@PathVariable("id") Long id) {
        boolean result = productFacade.deleteProduct(id);
        if (result) {
            log.debug("deleteBrand success :id={}", id);
            return CommonResult.success(null);
        } else {
            log.debug("deleteBrand failed :id={}", id);
            return CommonResult.failed("操作失败");
        }

    }

    @ApiOperation("获取指定id的商品")
    @GetMapping(value = "/{id}")
    @ResponseBody
    public CommonResult<Product> brand(@PathVariable("id") Long id) {
        return CommonResult.success(productFacade.getProduct(id));
    }
}

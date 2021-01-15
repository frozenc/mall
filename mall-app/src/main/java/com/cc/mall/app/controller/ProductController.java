package com.cc.mall.app.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.api.PageResult;
import com.cc.mall.common.utils.dto.ProductDto;
import com.cc.mall.common.utils.dto.page.ProductPageRequest;
import com.cc.mall.common.utils.vo.ProductVo;
import com.cc.mall.app.facade.ProductFacade;
import com.cc.mall.mbg.entity.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 品牌管理Controller
 * Created by cc on 2019/4/19.
 */
@Api(tags = "商品")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductFacade productFacade;


    @ApiOperation("获取所有商品列表")
    @GetMapping(value = "")
    @ResponseBody
    public PageResult<List<ProductVo>> getProductList(@Valid ProductPageRequest pageRequest) {
        Page<Product> page = productFacade.listProducts(pageRequest);
        List<ProductVo> productVos = Convert.convert(new TypeReference<List<ProductVo>>() {
        }, page.getRecords());
        return PageResult.success(productVos, page.getTotal());
    }
}

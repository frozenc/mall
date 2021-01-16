package com.cc.mall.app.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.mall.app.facade.OrderMasterFacade;
import com.cc.mall.common.jwt.JwtUserDetails;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.dto.page.OrderPageRequest;
import com.cc.mall.common.utils.vo.OrderDetailVo;
import com.cc.mall.common.utils.vo.OrderVo;
import com.cc.mall.mbg.entity.OrderDetail;
import com.cc.mall.mbg.entity.OrderMaster;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/13 20:34
 **/
@Api(tags = "订单")
@RestController
@RequestMapping("/order")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class OrderController {
    @Autowired
    private OrderMasterFacade orderMasterFacade;
    
    @ApiOperation("创建订单")
    @PostMapping("/create")
    public CommonResult<String> create(@ApiIgnore Authentication authentication) {
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        String orderId = orderMasterFacade.create(userDetails.getUser());
        return CommonResult.success(orderId);
    }
    
    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    public CommonResult<List<OrderVo>> list(@ApiIgnore Authentication authentication, @Valid OrderPageRequest pageRequest) {
        Page<OrderMaster> page = orderMasterFacade.list(pageRequest);
        List<OrderVo> orderVoList = Convert.convert(new TypeReference<List<OrderVo>>() {
        }, page.getRecords());
        return CommonResult.success(orderVoList);
    }


    @ApiOperation("获取订单详情")
    @GetMapping("/{id}")
    public CommonResult<List<OrderDetailVo>> getOrderInfo(@ApiIgnore Authentication authentication, @PathVariable("id") Long id) {
        List<OrderDetail> orderDetailList = orderMasterFacade.getDetails(authentication.getName(), id);
        List<OrderDetailVo> orderDetailVoList = Convert.convert(new TypeReference<List<OrderDetailVo>>() {
        }, orderDetailList);
        return CommonResult.success(orderDetailVoList);
    }
}

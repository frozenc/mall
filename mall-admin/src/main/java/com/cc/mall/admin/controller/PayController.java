package com.cc.mall.admin.controller;

import com.cc.mall.admin.facade.PayFacade;
import com.cc.mall.common.utils.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/16 17:09
 **/
@RestController
@Api("支付管理")
@RequestMapping("/pay")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PayController {

    @Autowired
    private PayFacade payFacade;

    @ApiOperation("订单处理退款")
    @PostMapping("/refund/{id}")
    public CommonResult<String> refund(@PathVariable("id") Long id) {
        payFacade.refund(id);
        return CommonResult.success();
    }

    @ApiOperation("订单关闭")
    @PostMapping("/close/{id}")
    public CommonResult<String> close(@PathVariable("id") Long id) {
        payFacade.close(id);
        return CommonResult.success();
    }

    @ApiOperation("订单发货")
    @PostMapping("/ship/{id}")
    public CommonResult<String> ship(@PathVariable("id") Long id) {
        payFacade.ship(id);
        return CommonResult.success();
    }
}

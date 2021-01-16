package com.cc.mall.app.controller;

import com.cc.mall.app.facade.PayFacade;
import com.cc.mall.common.utils.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/16 15:44
 **/
@Api(tags = "支付")
@RestController
@RequestMapping("/pay")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class PayController {

    @Autowired
    private PayFacade payFacade;

    @ApiOperation("订单支付")
    @PostMapping("/{id}")
    public void pay(@ApiIgnore Authentication authentication, @PathVariable("id") Long id,
                    HttpServletResponse response) {
        payFacade.pay(authentication.getName(), id, response);
    }

    @ApiOperation("订单关闭")
    @PostMapping("/close/{id}")
    public CommonResult<String> close(@ApiIgnore Authentication authentication, @PathVariable("id") Long id) {
        payFacade.close(authentication.getName(), id);
        return CommonResult.success();
    }

    @ApiOperation("确认收货")
    @PostMapping("/receive/{id}")
    public CommonResult<String> receive(@ApiIgnore Authentication authentication, @PathVariable("id") Long id) {
        payFacade.reveive(authentication.getName(), id);
        return CommonResult.success();
    }

    @ApiOperation("订单申请退款")
    @PostMapping("/refund/{id}")
    public CommonResult<String> refund(@ApiIgnore Authentication authentication, @PathVariable("id") Long id) {
        payFacade.refund(authentication.getName(), id);
        return CommonResult.success();
    }
}

package com.cc.mall.admin.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.mall.admin.facade.OrderMasterFacade;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.api.PageResult;
import com.cc.mall.common.utils.dto.page.OrderPageRequest;
import com.cc.mall.common.utils.dto.page.PageRequest;
import com.cc.mall.common.utils.vo.OrderDetailVo;
import com.cc.mall.common.utils.vo.OrderVo;
import com.cc.mall.mbg.entity.OrderDetail;
import com.cc.mall.mbg.entity.OrderMaster;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.ConstraintValidator;
import javax.validation.Valid;
import java.util.List;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/14 20:09
 **/
@Api(tags = "订单管理")
@RestController
@RequestMapping("/order")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class OrderMasterController {
    @Autowired
    private OrderMasterFacade orderMasterFacade;

    @ApiOperation("获取订单列表")
    @GetMapping("/list")
    public PageResult<List<OrderVo>> list(@Valid OrderPageRequest orderPageRequest) {
        Page<OrderMaster> orderMasterPage = orderMasterFacade.list(orderPageRequest);
        List<OrderVo> orderVoList = Convert.convert(new TypeReference<List<OrderVo>>() {
        }, orderMasterPage.getRecords());
        return PageResult.success(orderVoList, orderMasterPage.getTotal());
    }

    @ApiOperation("获取订单详情")
    @GetMapping("/{id}")
    public CommonResult<List<OrderDetailVo>> getDetail(@PathVariable("id") Long id) {
        List<OrderDetail> orderDetailList = orderMasterFacade.getDetails(id);
        List<OrderDetailVo> orderDetailVoList = Convert.convert(new TypeReference<List<OrderDetailVo>>() {
        }, orderDetailList);
        return CommonResult.success(orderDetailVoList);
    }
}

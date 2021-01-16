package com.cc.mall.app.controller;

import cn.hutool.core.convert.Convert;
import com.cc.mall.app.component.CartService;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.dto.CartDto;
import com.cc.mall.common.utils.vo.CartVo;
import com.cc.mall.mbg.entity.Product;
import com.cc.mall.mbg.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/13 20:16
 **/
@Api(tags = "购物车")
@RestController
@RequestMapping("/cart")
@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @ApiOperation("获取购物车")
    @GetMapping("")
    public CommonResult<List<CartVo>> list(@ApiIgnore Authentication authentication) {
        List<CartDto> cartDtoList = cartService.list(authentication.getName());
        List<CartVo> cartVoList = cartDtoList.stream()
                .map(cartDto -> {
                    Product product = productService.getById(cartDto.getId());
                    CartVo cartVo = Convert.convert(CartVo.class, product);
                    cartVo.setQuantity(cartDto.getQuantity());
                    cartVo.setChecked(cartDto.getChecked());
                    return cartVo;
                }).collect(Collectors.toList());
        return CommonResult.success(cartVoList);
    }

    @ApiOperation("加入购物车")
    @PostMapping("")
    public CommonResult<String> save(@ApiIgnore Authentication authentication, @Valid CartDto cartDto) {
        cartService.save(authentication.getName(), cartDto);
        return CommonResult.success();
    }

    @ApiOperation("更新购物车商品数量")
    @PutMapping("")
    public CommonResult<String> update(@ApiIgnore Authentication authentication, @Valid CartDto cartDto) {
        cartService.update(authentication.getName(), cartDto);
        return CommonResult.success();
    }

    @ApiOperation("删除购物车商品")
    @DeleteMapping("/{ids}")
    public CommonResult<String> delete(@ApiIgnore Authentication authentication, @PathVariable("ids") List<Long> ids) {
        cartService.delete(authentication.getName(), ids);
        return CommonResult.success();
    }

    @ApiOperation("更新选中购物车商品")
    @PutMapping("/check/{id}")
    public CommonResult<String> select(@ApiIgnore Authentication authentication, @PathVariable("id") Long id,
                                       @RequestParam("checked") Boolean checked) {
        if (Long.valueOf(0L).equals(id)) { //选中全部
            cartService.updateAllCheck(authentication.getName(), checked);
        } else { // 选中单个
            cartService.updateOneCheck(authentication.getName(), id, checked);
        }
        return CommonResult.success();
    }
}

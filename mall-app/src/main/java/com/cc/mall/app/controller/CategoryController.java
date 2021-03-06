package com.cc.mall.app.controller;

import cn.hutool.core.convert.Convert;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.dto.CategoryDto;
import com.cc.mall.common.utils.vo.CategoryVo;
import com.cc.mall.app.facade.CategoryFacade;
import com.cc.mall.mbg.entity.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * admin
 *
 * @author Chan
 * @since 2021/1/12 10:52
 **/
@Api(tags = "分类")
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryFacade categoryFacade;

    @ApiOperation("获取分类列表")
    @GetMapping("")
    public CommonResult<List<CategoryVo>> list(@RequestParam(name = "id", defaultValue = "0") Long id) {
        List<Category> categoryList = categoryFacade.list();
        List<CategoryVo> categoryVoList = get(categoryList, id);
        return CommonResult.success(categoryVoList);
    }

    //循环嵌套调用
    public List<CategoryVo> get(List<Category> categoryList, Long id) {
        List<CategoryVo> categoryVos = getById(categoryList, id);
        //子分类
        categoryVos.forEach(categoryVo -> categoryVo.setChildren(get(categoryList, categoryVo.getId())));
        return categoryVos;
    }

    public List<CategoryVo> getById(List<Category> categoryList, Long id) {
        return categoryList.stream()
                .filter(category -> category.getPid().equals(id))
                .map(category -> Convert.convert(CategoryVo.class, category))
                .collect(Collectors.toList());
    }
}

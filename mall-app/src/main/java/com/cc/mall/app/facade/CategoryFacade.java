package com.cc.mall.app.facade;

import com.cc.mall.common.utils.dto.CategoryDto;
import com.cc.mall.mbg.entity.Category;
import com.cc.mall.mbg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * admin
 *
 * @author Chan
 * @since 2021/1/12 9:59
 **/
@Service
@Transactional
public class CategoryFacade {
    @Autowired
    private CategoryService categoryService;

    @Cacheable(value = "category") //EnableCaching
    public List<Category> list() {
        return categoryService.list();
    }
}

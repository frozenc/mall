package com.cc.mall.admin.facade;

import cn.hutool.core.convert.Convert;
import com.cc.mall.common.utils.dto.CategoryDto;
import com.cc.mall.mbg.entity.Category;
import com.cc.mall.mbg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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

    @CacheEvict(value = "category", allEntries = true) // clear cache
    public void save(CategoryDto categoryDto) {
        Category category = Convert.convert(Category.class, categoryDto);
        categoryService.save(category);
    }

    @CacheEvict(value = "category", allEntries = true) //clear cache
    public void update(Long id, CategoryDto categoryDto) {
        Category category = Convert.convert(Category.class, categoryDto).setId(id);
        categoryService.updateById(category);
    }

    @CacheEvict(value = "category", allEntries = true) //clear cache
    public void delete(Long id) {
        categoryService.removeById(id);
    }
}

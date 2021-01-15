package com.cc.mall.mbg.service.impl;

import com.cc.mall.mbg.entity.Product;
import com.cc.mall.mbg.mapper.ProductMapper;
import com.cc.mall.mbg.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cc
 * @since 2021-01-12
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}

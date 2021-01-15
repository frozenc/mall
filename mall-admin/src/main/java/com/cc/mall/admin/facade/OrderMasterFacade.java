package com.cc.mall.admin.facade;

import cn.hutool.core.lang.Snowflake;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.mall.admin.dao.StockDao;
import com.cc.mall.common.base.GlobalException;
import com.cc.mall.common.component.redis.service.RedisService;
import com.cc.mall.common.utils.api.ResultCode;
import com.cc.mall.common.utils.dto.CartDto;
import com.cc.mall.common.utils.dto.page.OrderPageRequest;
import com.cc.mall.common.utils.utils.Constants;
import com.cc.mall.mbg.entity.OrderDetail;
import com.cc.mall.mbg.entity.OrderMaster;
import com.cc.mall.mbg.entity.User;
import com.cc.mall.mbg.service.OrderDetailService;
import com.cc.mall.mbg.service.OrderMasterService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * mall-admin
 *
 * @author Chan
 * @since 2021/1/13 15:00
 **/
@Service
@Transactional
public class OrderMasterFacade {

    @Autowired
    private StockDao stockDao;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private RedisService redisService;

    // Lua脚本
    private final DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(Constants.LUA_SCRIPT, Long.class);

    public OrderMaster get(Long id) {
        return orderMasterService.getById(id);
    }

    public List<OrderDetail> getDetails(Long id) {
        return orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, id));
    }

    /**
     * 分页查询订单
     */
    public Page<OrderMaster> list(OrderPageRequest orderPageRequest) {
        Page<OrderMaster> page = new Page<>(orderPageRequest.getPageNum(), orderPageRequest.getPageSize());
        QueryWrapper<OrderMaster> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(orderPageRequest.getStatus())) {
            queryWrapper.lambda().eq(OrderMaster::getStatus, orderPageRequest.getStatus());
        }
        queryWrapper.lambda().orderByDesc(OrderMaster::getCreateTime);
        return orderMasterService.page(page, queryWrapper);
    }

    /**
     * 更改订单状态
     */
    public void updateOrderStatus(Long id, Integer status) {
        OrderMaster orderMaster = new OrderMaster()
                .setId(id)
                .setStatus(status);
        orderMasterService.updateById(orderMaster);
    }

    /**
     * 库存返回
     */
    public void returnStock(Long id) {
        List<OrderDetail> orderDetails = getDetails(id);
        orderDetails.forEach(orderDetail -> {
            stockDao.addStock(orderDetail.getProductId(), orderDetail.getProductQuantity());
            redisService.increment(Constants.PRODUCT_STOCK + orderDetail.getProductId(),
                    orderDetail.getProductQuantity());
        });
    }

}

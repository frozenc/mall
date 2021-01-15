package com.cc.mall.app.facade;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import com.alibaba.druid.wall.violation.ErrorCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cc.mall.app.component.CartService;
import com.cc.mall.app.component.mq.OrderMessage;
import com.cc.mall.app.dao.StockDao;
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
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.apache.commons.lang3.time.*;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/13 15:09
 **/
@Service
@Transactional
public class OrderMasterFacade {
    @Autowired
    private Snowflake snowflake;

    @Autowired
    private StockDao stockDao;

    @Autowired
    private OrderMasterService orderMasterService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private CartService cartService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    // Lua脚本
    private final DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(Constants.LUA_SCRIPT, Long.class);

    public OrderMaster get(Long id) {
        return orderMasterService.getById(id);
    }

    public OrderMaster get(String username, Long id) {
        OrderMaster orderMaster = get(id);
        if (orderMaster != null && orderMaster.getUsername().equals(username)) {
            return orderMaster;
        }
        throw new GlobalException(ResultCode.ORDER_NOT_EXIST);
    }

    public List<OrderDetail> getDetails(Long id) {
        return orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, id));
    }

    public List<OrderDetail> getDetails(String username, Long id) {
        //确保订单存在
        get(username, id);
        return getDetails(id);
    }

    public String create(User user) {
        //购物车,只要处于选中状态的商品
        List<CartDto> cartDtoList = cartService.list(user.getUsername()).stream()
                .filter(CartDto::getChecked).collect(Collectors.toList());
        if (cartDtoList.size() == 0) {
            throw new GlobalException(ResultCode.CART_EMPTY);
        }

        //记住购物ID和数量
        Map<Long, Integer> cart = new HashMap<>();
        List<String> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        for (CartDto cartDto:cartDtoList) {
            cart.put(cartDto.getId(), cartDto.getQuantity());
            keys.add(Constants.PRODUCT_STOCK + cartDto.getId());
            values.add(cartDto.getQuantity());
        }

        //LUA原子脚本预减库存
        Long result = redisService.execute(redisScript, keys, values.toArray());
        //库存不足
        if (result == 0) {
            throw new GlobalException(ResultCode.PRODUCT_STOCK_NOT_ENOUGH);
        }

        //清空购物车
        List<Long> ids = cartDtoList.stream()
                .filter(CartDto::getChecked)
                .map(CartDto::getId)
                .collect(Collectors.toList());
        cartService.delete(user.getUsername(), ids);

        //雪花算法，生成全局唯一ID
        Long orderId = snowflake.nextId();
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrderId(orderId);
        orderMessage.setUser(user);
        orderMessage.setCart(cart);

        //发送同步订单消息
        rocketMQTemplate.syncSend(OrderMessage.TOPIC, orderMessage);

        //事务操作
        //rocketMQTemplate.sendMessageInTransaction(orderMessage.TOPIC, MessageBuilder.withPayload(orderMessage).build(), null);

        return String.valueOf(orderId);
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


    /**
     * 查看时间点后的支付列表
     */
    public List<OrderMaster> payList(long delta) {
        String date = DateFormatUtils.format(new Date().getTime() - delta, "yyyy-MM-dd HH:mm:ss");
        return orderMasterService.list(Wrappers.<OrderMaster>lambdaQuery()
                .select(OrderMaster::getId)
                .apply("UNIX_TIMESTAMP(update_time) >= UNIX_TIMESTAMP('" + date + "')"));
    }
}

package com.cc.mall.app.component;

import com.cc.mall.common.utils.dto.CartDto;
import com.cc.mall.common.utils.utils.Constants;
import com.cc.mall.common.component.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/13 15:16
 **/
@Service
public class CartService {
    @Autowired
    private RedisService redisService;

    /**
     * 获取购物车中所有商品
     */
    public List<CartDto> list(String username) {
        Set<String> keys = redisService.keys(Constants.PRODUCT_CART + username);
        List<Object> objects = redisService.multiGet(keys);
        return objects.stream()
                .map(object -> (CartDto)object)
                .collect(Collectors.toList());
    }

    /**
     * 加入购物车
     */
    public void save(String username, CartDto cartDto) {
        String key = getKey(username, cartDto.getId());
        redisService.set(key, cartDto);
    }

    /**
     * 更新购物车中商品数量
     */
    public void update(String username, CartDto cartDto) {
        String key = getKey(username, cartDto.getId());
        CartDto redisCart = (CartDto) redisService.get(key);
        redisCart.setQuantity(cartDto.getQuantity());
        redisService.set(key, redisCart);
    }

    /**
     * 将商品移除购物车
     */
    public void delete(String username, List<Long> ids) {
        List<String> keys = ids.stream().map(id -> getKey(username, id)).collect(Collectors.toList());
        redisService.multiDelete(keys);
    }

    /**
     * 更新购物车中全部商品选中状态
     */
    public void updateAllCheck(String username, Boolean checked) {
        Set<String> keys = redisService.keys(Constants.PRODUCT_CART + username);
        keys.forEach(key -> updateCheck(key, checked));

    }

    /**
     * 更新购物车中特定商品选中状态
     */
    public void updateOneCheck(String username, Long id, Boolean checked) {
        String key = getKey(username, id);
        updateCheck(key, checked);
    }

    /**
     *更新购物车中商品选中状态
     */
    public void updateCheck(String key, Boolean checked) {
        CartDto cartDto = (CartDto) redisService.get(key);
        cartDto.setChecked(checked);
        redisService.set(key, cartDto);
    }

    private String getKey(String username, Long productId) {
        return Constants.PRODUCT_CART + username + ":" + productId;
    }
}

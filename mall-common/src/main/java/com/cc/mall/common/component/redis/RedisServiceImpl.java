package com.cc.mall.common.component.redis;

import com.cc.mall.common.component.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * admin
 * redis操作service
 *
 * @author Chan
 * @since 2021/1/11 11:30
 **/
@Component
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public List<Object> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object value, long expire) {
        set(key, value, expire, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value, long expire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    @Override
    public void multiSet(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void multiDelete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    @Override
    public Long increment(String key, int delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Long decrement(String key, int delta) {
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     *
     * @param key 模糊查询的key值
     * @return 返回key值集合
     */
    @Override
    public Set<String> keys(String key) {
        return redisTemplate.keys(key + "*");
    }

    @Override
    public <T> T execute(RedisScript<T> redisScript, List<String> keys, Object... args) {
        return redisTemplate.execute(redisScript, keys, args);
    }
}

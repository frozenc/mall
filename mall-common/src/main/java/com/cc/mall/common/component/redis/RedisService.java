package com.cc.mall.common.component.redis;

import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * admin
 *
 * @author Chan
 * @since 2021/1/11 11:38
 **/
public interface RedisService {
    /**
     * 获取
     */
    Object get(String key);

    /**
     * 批量获取
     */
    List<Object> multiGet(Collection<String> keys);

    /**
     * 设置
     */
    void set(String key, Object value);

    /**
     * 设置
     */
    void set(String key, Object value, long expire);

    /**
     * 设置
     */
    void set(String key, Object value, long expire, TimeUnit timeUnit);

    /**
     * 批量设置
     */
    void multiSet(Map<String, Object> map);

    /**
     * 删除
     */
    void delete(String key);

    /**
     * 批量删除
     */
    void multiDelete(Collection<String> keys);

    /**
     * value + delta
     */
    Long increment(String key, int delta);

    /**
     * value - delta
     */
    Long decrement(String key, int delta);

    /**
     * 是否存在key
     * @param key
     * @return
     */
    public Boolean hasKey(String key);

    /**
     * keys查询
     */
    Set<String> keys(String key);

    /**
     * LUA原子脚本
     */
    <T> T execute(RedisScript<T> redisScript, List<String> keys, Object... args);
}

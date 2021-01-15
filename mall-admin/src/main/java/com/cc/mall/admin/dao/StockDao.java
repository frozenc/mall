package com.cc.mall.admin.dao;

import org.apache.ibatis.annotations.Update;

/**
 * mall-app
 *
 * @author Chan
 * @since 2021/1/13 15:07
 **/
public interface StockDao {

    @Update("UPDATE product SET stock=stock+#{count} WHERE id=#{id}")
    boolean addStock(Long id, int count);

    @Update("UPDATE product SET stock=stock-#{count} WHERE id=#{id}")
    boolean subStock(Long id, int count);
}

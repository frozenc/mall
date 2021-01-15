package com.cc.mall.common.utils.api;

import lombok.Getter;

/**
 * admin
 *
 * @author Chan
 * @since 2021/1/12 13:35
 **/
@Getter
public class PageResult<T> extends CommonResult<T> {

    private Long count;

    public PageResult(Long code, String msg, T data, Long count) {
        super(code, msg, data);
        this.count = count;
    }

    public static <T> PageResult<T> success(T data, Long count) {
        return new PageResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data, count);
    }
}

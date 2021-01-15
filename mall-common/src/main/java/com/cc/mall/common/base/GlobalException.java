package com.cc.mall.common.base;

import com.cc.mall.common.utils.api.ResultCode;

public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ResultCode resultCode;

    public GlobalException(ResultCode resultCode) {
        super(resultCode.toString());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
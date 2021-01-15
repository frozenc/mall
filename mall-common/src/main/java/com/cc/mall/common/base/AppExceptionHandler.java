package com.cc.mall.common.base;

import com.alibaba.druid.wall.violation.ErrorCode;
import com.cc.mall.common.utils.api.CommonResult;
import com.cc.mall.common.utils.api.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = GlobalException.class)
    @ResponseBody
    public CommonResult<String> exceptionHandler(HttpServletRequest request, HttpServletResponse response, GlobalException e) {
        log.error(e.getMessage());
        return CommonResult.failed(e.getResultCode());
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResult<String> exceptionHandler(HttpServletRequest request, HttpServletResponse response, BindException e) {
        log.error(e.getMessage());
        List<ObjectError> errorList = e.getAllErrors();
        String msg = errorList.get(0).getDefaultMessage();
        return CommonResult.failed(ResultCode.BIND_EXCEPTION.getCode(), msg);
    }
}


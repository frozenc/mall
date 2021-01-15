package com.cc.mall.common.utils.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RegisterDto {

    @ApiModelProperty(value = "用户名", required = true)
    @Size(min = 3, max = 12, message = "用户名长度为3-12")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @Size(min = 3, max = 12, message = "密码长度为3-12")
    private String password;

    @ApiModelProperty(value = "验证码key")
    @NotEmpty(message = "验证码key不能为空")
    private String key;

    @ApiModelProperty(value = "验证码code")
    @NotEmpty(message = "验证码code不能为空")
    private String code;
}

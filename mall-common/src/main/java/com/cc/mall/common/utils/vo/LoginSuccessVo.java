package com.cc.mall.common.utils.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LoginSuccessVo extends UserInfoVo {

    private String token;
}

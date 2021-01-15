package com.cc.mall.common.utils.dto.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderPageRequest extends PageRequest {

    @ApiModelProperty(value = "状态")
    private String status = "";
}

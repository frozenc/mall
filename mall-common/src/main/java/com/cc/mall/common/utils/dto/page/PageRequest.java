package com.cc.mall.common.utils.dto.page;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageRequest {

    @ApiModelProperty(value = "页面", example = "0")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "大小", example = "0")
    private Integer pageSize = 10;
}

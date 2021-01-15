package com.cc.mall.common.utils.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class CategoryDto {

    @ApiModelProperty(value = "分类")
    @Size(min = 1, max = 255, message = "分类长度为1-255")
    private String name = "未命名";

    @ApiModelProperty(value = "PID", example = "0")
    private Long pid = 0L;
}

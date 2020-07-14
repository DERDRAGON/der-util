package com.der.dertool.constants;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-14 12:05
 */
@Data
@ApiModel(description = "分页工具")
public class Pager implements Serializable {
    @ApiModelProperty(hidden = true)
    private static final long serialVersionUID = -4065841388751571253L;

    @ApiModelProperty(value = "页数", example = "1")
    private Integer pageNo;

    @ApiModelProperty(value = "每页数量", example = "20")
    private Integer pageSize;
}

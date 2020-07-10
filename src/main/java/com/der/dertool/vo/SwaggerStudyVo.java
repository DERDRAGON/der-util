package com.der.dertool.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-08 17:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "swaggerStudyVo", description = "swagger的学习OV描述")
public class SwaggerStudyVo {

    @ApiModelProperty(value = "这是VO的主键", name = "ID", notes = "这是一个参数ID", required = true, position = 1)
    private Integer id;

    @ApiModelProperty(value = "VO的名称", notes = "这是一个参数name", position = 2)
    private String name;

    @ApiModelProperty(notes = "这是一个参数height", position = 2)
    private double height;

    @ApiModelProperty(value = "VO的性别", notes = "这是一个参数sex", position = 2)
    private boolean sex;
}

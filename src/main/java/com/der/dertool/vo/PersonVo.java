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
 * @create: 2020-07-06 11:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Class representing a person tracked by the application.")
public class PersonVo {
    @ApiModelProperty(notes = "Unique identifier of the person. No two persons can have the same id.", example = "1", required = true, position = 0)
    private int id;
    @ApiModelProperty(notes = "First name of the person.", example = "John", required = true, position = 1)
    private String firstName;
    @ApiModelProperty(notes = "Last name of the person.", example = "Doe", required = true, position = 2)
    private String lastName;
    @ApiModelProperty(notes = "Age of the person. Non-negative integer", example = "42", position = 3)
    private int age;
}

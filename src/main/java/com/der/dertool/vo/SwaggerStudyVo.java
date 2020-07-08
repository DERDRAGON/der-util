package com.der.dertool.vo;

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
public class SwaggerStudyVo {

    private Integer id;

    private String name;

    private double height;

    private boolean sex;
}

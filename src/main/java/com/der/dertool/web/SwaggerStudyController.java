package com.der.dertool.web;

import com.der.dertool.constants.DerResponse;
import com.der.dertool.vo.SwaggerStudyVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: der-tool
 * @description: swagger学习的控制层
 * @author: long
 * @create: 2020-07-08 15:55
 */
@Api(value = "swagger-study", description = "一个swagger的学习样例", tags = {"swagger操作学习接口"}, basePath = "der-tool", position = 1)
@RestController
@RequestMapping(value = "study-swagger")
public class SwaggerStudyController {

    /**
     * 初始化数据集合
     */
    private static final List<SwaggerStudyVo> INIT_DATA = Lists.newArrayList(
            new SwaggerStudyVo(0, "零", 100.0, true),
            new SwaggerStudyVo(1, "壹", 110.0, false),
            new SwaggerStudyVo(2, "贰", 120.0, true)
    );

    @RequestMapping(value = "getbyid/{id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "根据实体ID获取实体信息，没有则新建一个",
//            tags = {"根据实体ID获取实体信息，从初始化数据集合中获取", "没有则新建一个，原始数据集合保持不变"},
            position = 1,
            response = DerResponse.class,
//            responseContainer = ""
            nickname = "这是一个ID查询的接口",
            httpMethod = "GET"
    )
    public DerResponse<SwaggerStudyVo> getVoById(@PathVariable("id") Integer id) {
        SwaggerStudyVo studyVo = INIT_DATA.get(id);
        if (null == studyVo) {
            studyVo = new SwaggerStudyVo();
            studyVo.setId(id);
        }
        return DerResponse.openSuccess(studyVo);
    }

}

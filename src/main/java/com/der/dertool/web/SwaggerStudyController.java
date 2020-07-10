package com.der.dertool.web;

import com.der.dertool.constants.DerResponse;
import com.der.dertool.vo.SwaggerStudyVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.*;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: der-tool
 * @description: swagger学习的控制层
 * @author: long
 * @create: 2020-07-08 15:55
 */
@Api(value = "swagger-study", description = "一个swagger的学习样例", tags = {"swagger操作学习接口"}, basePath = "der-tool")
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
            notes = "根据实体ID获取实体信息",
//            tags = {"根据实体ID获取实体信息，从初始化数据集合中获取", "没有则新建一个，原始数据集合保持不变"},
            response = DerResponse.class,
//            responseContainer = ""
//            nickname = "这是一个ID查询的接口",
            httpMethod = "GET"
    )
    public DerResponse<SwaggerStudyVo> getVoById(@ApiParam(name = "id", value = "列表某一个索引", defaultValue = "2", allowableValues = "0,1,2") @PathVariable("id") Integer id) {
        SwaggerStudyVo studyVo = null;
        if (id >= INIT_DATA.size() || id < 0) {
            studyVo = new SwaggerStudyVo();
            studyVo.setId(id);
        } else {
            studyVo = INIT_DATA.get(id);
        }
        return DerResponse.openSuccess(studyVo);
    }

    @ApiOperation(
            value = "获取vo的列表",
//            response = DerResponse.class,
            httpMethod = "POST",
            extensions = @Extension(properties ={
                    @ExtensionProperty(name = "caoshilong", value = "caoshilong@xx.com")
            })
    )
//    @ResponseHeader(name = "author", description = "作者", response = DerResponse.class)
    @RequestMapping(value = "list", method = {RequestMethod.POST})
    public DerResponse<List<SwaggerStudyVo>> getVoList() {
        return DerResponse.openSuccess(INIT_DATA);
    }

    @ApiOperation(
            value = "修改VO的操作",
            response = DerResponse.class,
            httpMethod = "POST",
            extensions = @Extension(properties ={
                    @ExtensionProperty(name = "caoshilong", value = "caoshilong@xx.com")
            })
    )
    @ApiResponses({ @ApiResponse(code = 500, message = "修改失败，无对应的参数") })
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public DerResponse change(@ApiParam(value = "swagger 的vo信息") @RequestBody SwaggerStudyVo swaggerStudyVo) {
        List<SwaggerStudyVo> list = INIT_DATA.stream().filter(swagger -> swagger.getId().equals(swaggerStudyVo.getId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)) {
            return DerResponse.openFail();
        }
        list.forEach(swagger -> {
            swagger.setName(swaggerStudyVo.getName());
            swagger.setHeight(swaggerStudyVo.getHeight());
            swagger.setSex(swaggerStudyVo.isSex());
        });
        return DerResponse.openSuccess();
    }

}

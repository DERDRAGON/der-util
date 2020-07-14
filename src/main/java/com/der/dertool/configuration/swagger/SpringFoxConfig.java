package com.der.dertool.configuration.swagger;

import com.der.dertool.enums.StatusCode;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-06 11:23
 */
@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "swagger",value = {"enabled"}, havingValue = "true")
public class SpringFoxConfig {

    @Bean
    public Docket apiDocket() {
        List<ResponseMessage> responseMessageList = Lists.newArrayList();
        for (StatusCode statusCode : StatusCode.values()) {
            responseMessageList.add(new ResponseMessageBuilder().code(statusCode.getCode()).message(statusCode.getMsg()).responseModel(new ModelRef("DerResponse")).build());
        }
        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.der.dertool.web"))
//                .paths(PathSelectors.ant("/v2/**"))
                .paths(PathSelectors.any())
                .build().apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder().title("这是一个工具演示项目")
                .description("这是一个工具演示项目 的 描述")
                .version("V1.0.0").contact(new Contact("曹世龙", "https://gitee.com/dragonxiao/projects", "1297886375@qq.com")).build();
//        return new ApiInfo("这是一个工具演示项目", "这是一个工具演示项目 的 描述", "V1.0.0", "http://localhost:9000/platformgroup/ms-admin", new Contact("曹世龙", "https://gitee.com/dragonxiao/projects", "1297886375@qq.com"), "LICENSE", "LICENSE URL", Collections.emptyList());
    }
}

- [Swagger2一个DTO 适用于多个接口](#swagger2一个dto-适用于多个接口)
  - [自定义注解](#自定义注解)
  - [自定义swagger插件](#自定义swagger插件)
  - [使用示例](#使用示例)

# Swagger2一个DTO 适用于多个接口

## 自定义注解

```java
package com.der.dertool.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: der-tool
 * @description: 针对swagger2 的自定义注解
 * @author: long
 * @create: 2020-07-13 16:41
 */

@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParamInclude {

    /**
     * 内容值
     * @return values
     */
    String[] value();

}
```

## 自定义swagger插件

```java
package com.der.dertool.configuration.swagger;

import com.alibaba.ttl.internal.javassist.ClassPool;
import com.alibaba.ttl.internal.javassist.CtClass;
import com.alibaba.ttl.internal.javassist.CtField;
import com.alibaba.ttl.internal.javassist.NotFoundException;
import com.alibaba.ttl.internal.javassist.bytecode.AnnotationsAttribute;
import com.alibaba.ttl.internal.javassist.bytecode.ConstPool;
import com.alibaba.ttl.internal.javassist.bytecode.annotation.Annotation;
import com.alibaba.ttl.internal.javassist.bytecode.annotation.StringMemberValue;
import com.der.dertool.annotations.ApiParamInclude;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ParameterBuilderPlugin;
import springfox.documentation.spi.service.contexts.ParameterContext;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-13 16:50
 */
@Component
@Order
public class MapApiReader implements ParameterBuilderPlugin {

    @Resource
    private TypeResolver typeResolver;

    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        Optional<ApiParamInclude> annotation = methodParameter.findAnnotation(ApiParamInclude.class);
        Class originClass = parameterContext.resolvedMethodParameter().getParameterType().getErasedType();
        if (annotation.isPresent()) {
            Random random = new Random();
            String name = originClass.getSimpleName() + "Model" + random.nextInt(100);  //model 名称
            String[] properties = annotation.get().value();
            try {
                parameterContext.getDocumentationContext()
                        .getAdditionalModels()
                        .add(typeResolver.resolve(createRefModelInclude(properties, originClass.getPackage()+"."+name, originClass)));  //像documentContext的Models中添加我们新生成的Class
            } catch (Exception e) {
                e.printStackTrace();
            }
            parameterContext.parameterBuilder()  //修改Map参数的ModelRef为我们动态生成的class
                    .parameterType("body")
                    .modelRef(new ModelRef(name))
                    .name(name);
        }
    }

    /**
     * 创建apiModel
     * @param propertys 包含属性
     * @param name 类名
     * @param origin 来源类
     * @return apiModel子类
     * @throws NotFoundException 类找不到
     */
    private Class createRefModelInclude(String[] propertys, String name, Class origin) throws NotFoundException {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass(name);
        try {
            Field[] fields = origin.getDeclaredFields();
            List<Field> fieldList = Arrays.asList(fields);
            List<String> includePropertys = Arrays.asList(propertys);
            List<Field> dealFileds = fieldList.stream().filter(s -> includePropertys.contains(s.getName())).collect(Collectors.toList());
            for (Field field : dealFileds) {
                CtField ctField = new CtField(ClassPool.getDefault().get(field.getType().getName()), field.getName(), ctClass);
                ctField.setModifiers(Modifier.PUBLIC);
                ApiModelProperty ampAnno = origin.getDeclaredField(field.getName()).getAnnotation(ApiModelProperty.class);
                String attributes = java.util.Optional.ofNullable(ampAnno).map(s->s.value()).orElse("");
                //添加model属性说明
                if (StringUtils.isNotBlank(attributes) ){
                    ConstPool constPool = ctClass.getClassFile().getConstPool();
                    AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
                    Annotation ann = new Annotation(ApiModelProperty.class.getName(), constPool);
                    ann.addMemberValue("value", new StringMemberValue(attributes, constPool));
                    attr.addAnnotation(ann);
                    ctField.getFieldInfo().addAttribute(attr);
                }
                ctClass.addField(ctField);
            }
            return ctClass.toClass();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}
```

## 使用示例

```java
package com.der.dertool.web;

import com.der.dertool.annotations.ApiParamInclude;
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

    @ApiOperation(
            value = "修改VO的操作",
            response = DerResponse.class,
            httpMethod = "POST",
            extensions = @Extension(properties ={
                    @ExtensionProperty(name = "caoshilong", value = "caoshilong@xx.com")
            })
    )
    @ApiResponses({ @ApiResponse(code = 500, message = "调用失败", response = DerResponse.class) })
    @RequestMapping(value = "change", method = RequestMethod.POST)
    public DerResponse change(@ApiParam(value = "swagger 的vo信息") @ApiParamInclude({"id", "name"}) @RequestBody SwaggerStudyVo swaggerStudyVo) {
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
```


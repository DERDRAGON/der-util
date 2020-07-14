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
import com.der.dertool.util.RandomNumUtils;
import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
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

    /**
     * 字段类名联合内部类
     */
    @AllArgsConstructor
    class ClazzFieldCombine {

        /**
         * 字段名
         */
        @Getter
        private Field field;

        /**
         * 类
         */
        @Getter
        private Class aClass;
    }

    @Override
    public void apply(ParameterContext parameterContext) {
        ResolvedMethodParameter methodParameter = parameterContext.resolvedMethodParameter();
        Optional<ApiParamInclude> annotation = methodParameter.findAnnotation(ApiParamInclude.class);
        Class originClass = parameterContext.resolvedMethodParameter().getParameterType().getErasedType();
        if (annotation.isPresent()) {
            //model 名称
            String name = originClass.getSimpleName() + "Model" + RandomNumUtils.getNextInHundred();
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
            List<String> includePropertys = Arrays.asList(propertys);
            List<ClazzFieldCombine> list = getAllCombine(includePropertys, origin);
            for (ClazzFieldCombine combine : list) {
                Field field = combine.getField();
                Class aClass = combine.getAClass();
                CtField ctField = new CtField(ClassPool.getDefault().get(field.getType().getName()), field.getName(), ctClass);
                ctField.setModifiers(Modifier.PUBLIC);
                ApiModelProperty ampAnno = aClass.getDeclaredField(field.getName()).getAnnotation(ApiModelProperty.class);
                String attributes = java.util.Optional.ofNullable(ampAnno).map(s->s.value()).orElse("");
                //添加model属性说明
                if (StringUtils.isNotBlank(attributes)){
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

    /**
     * 获取所有内部字段并过滤（包括父类）
     * @param includePropertys 需要的字段
     * @param origin 原生类
     * @return 过滤后的内容
     */
    private List<ClazzFieldCombine> getAllCombine(final List<String> includePropertys, Class origin) {
        final List<ClazzFieldCombine> clazzFieldCombines = Lists.newArrayList();
        // 类不为空且不等于Object的这个顶级父类
        while (null != origin && !(origin  == Object.class)) {
            final Class combineClazz =  origin;
            List<ClazzFieldCombine> combineList = Lists.newArrayList(combineClazz.getDeclaredFields()).stream()
                    //过滤需要的属性(include中包含，且子类field中没有的)
                    .filter(s -> includePropertys.contains(s.getName()) && clazzFieldCombines.stream().noneMatch(combine -> s.getName().equals(combine.getField().getName())))
                    //转化为工具类
                    .map(field -> new ClazzFieldCombine(field, combineClazz)).collect(Collectors.toList());
            clazzFieldCombines.addAll(combineList);
            origin = origin.getSuperclass();
        }
        return clazzFieldCombines;
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}

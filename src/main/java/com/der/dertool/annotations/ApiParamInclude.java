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

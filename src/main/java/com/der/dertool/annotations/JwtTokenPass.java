package com.der.dertool.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-22 16:30
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtTokenPass {

    /**
     * 是否需要验证权限
     * @return
     */
    boolean required() default true;

}

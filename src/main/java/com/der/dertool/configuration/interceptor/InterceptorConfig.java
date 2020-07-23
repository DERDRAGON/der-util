package com.der.dertool.configuration.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-22 17:08
 */
//@Configuration
//public class InterceptorConfig extends WebMvcConfigurerAdapter {
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authenticationInterceptor())
//                .excludePathPatterns("/swagger/**", "/v2/api-docs", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**")
//                .addPathPatterns("/**");    // 拦截所有请求，通过判断是否有 @JwtTokenPass 注解 决定是否需要登录
//    }
//
//    public AuthenticationInterceptor authenticationInterceptor() {
//        return new AuthenticationInterceptor();
//    }
//}
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                //排除掉swagger的相关请求
                .excludePathPatterns("/swagger/**", "/v2/api-docs", "/swagger-ui.html", "/swagger-resources/**", "/webjars/**")
                .addPathPatterns("/**");    // 拦截所有请求，通过判断是否有 @JwtTokenPass 注解 决定是否需要登录
    }

    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
}

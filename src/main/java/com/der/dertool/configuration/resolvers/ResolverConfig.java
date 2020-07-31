package com.der.dertool.configuration.resolvers;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-31 09:32
 */
@Configuration
public class ResolverConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new DerRequestParamMethodArgumentResolver());
    }

}

package com.der.dertool.configuration.filter;

import com.der.dertool.configuration.servlet.BodyReadHttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-30 17:21
 */
@Slf4j
@WebFilter(filterName = "bodyReaderFilter",urlPatterns = "/*")
public class BodyReaderFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("自定义过滤器操作开始");
        chain.doFilter(new BodyReadHttpServletRequestWrapper((HttpServletRequest) request), response);
        log.info("自定义过滤器操作结束");
    }

}

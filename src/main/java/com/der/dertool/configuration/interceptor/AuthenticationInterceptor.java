package com.der.dertool.configuration.interceptor;

import com.alibaba.fastjson.JSON;
import com.der.dertool.annotations.JwtTokenPass;
import com.der.dertool.constants.DerResponse;
import com.der.dertool.enums.StatusCode;
import com.der.dertool.util.JwtUtils;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-22 16:23
 */

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod= (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //检查是否有权限验证
        if (method.isAnnotationPresent(JwtTokenPass.class)) {
            return true;
        }
        // 从 cookie中取出access_token
        if (null == request.getCookies() || request.getCookies().length == 0) {
            feedbackLoginFail(response);
            return false;
        }
        List<Cookie> cookies = Lists.newArrayList(request.getCookies());
        List<String> tokens = cookies.stream().filter(cookie -> "access_token".equals(cookie.getName())).map(Cookie::getValue).collect(Collectors.toList());
        String token = CollectionUtils.isEmpty(tokens)? null:tokens.get(0);
        if (StringUtils.isBlank(token)) {
            feedbackLoginFail(response, cookies);
            return false;
        }
        String userName = JwtUtils.getUserName(token);
        if (StringUtils.isBlank(userName)) {
            feedbackLoginFail(response, cookies);
            return false;
        }
        //username = K0790016 password = Admin@123
        if (!JwtUtils.verify(token, userName, "Admin@123")) {
            feedbackLoginFail(response, cookies);
            return false;
        }
        return true;
        //检查有没有需要用户权限的注解
//        if (method.isAnnotationPresent(UserLoginToken.class)) {
//            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
//            if (userLoginToken.required()) {
//                // 执行认证
//                if (token == null) {
//                    throw new RuntimeException("无token，请重新登录");
//                }
//                // 获取 token 中的 user id
//                String userId;
//                try {
//                    userId = JWT.decode(token).getAudience().get(0);
//                } catch (JWTDecodeException j) {
//                    throw new RuntimeException("401");
//                }
//                User user = userService.findUserById(userId);
//                if (user == null) {
//                    throw new RuntimeException("用户不存在，请重新登录");
//                }
//                // 验证 token
//                JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
//                try {
//                    jwtVerifier.verify(token);
//                } catch (JWTVerificationException e) {
//                    throw new RuntimeException("401");
//                }
//                return true;
//            }
//        }
    }

    /**
     * 返回response输出无用户
     * @param response
     */
    private void feedbackLoginFail(HttpServletResponse response) {
        feedbackLoginFail(response, null);
    }

    /**
     * 设置cookie过期，返回response输出无用户
     * @param response httpresponse
     * @param cookies 浏览器cookies
     */
    private void feedbackLoginFail(HttpServletResponse response, List<Cookie> cookies) {
        if (!CollectionUtils.isEmpty(cookies)) {
            cookies.forEach(cookie -> cookie.setMaxAge(0));
        }
        response.setCharacterEncoding(Charsets.UTF_8.name());
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
        try {
            response.getOutputStream().write(JSON.toJSONString(DerResponse.openFail(StatusCode.NO_USER_ERROR)).getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

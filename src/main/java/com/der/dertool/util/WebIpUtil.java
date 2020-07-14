package com.der.dertool.util;

import com.der.dertool.vo.SwaggerStudyVo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-06 09:24
 */
public class WebIpUtil {

    public static final String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static void main(String[] args) {
        Field[] fields = SwaggerStudyVo.class.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }
        Class<? super SwaggerStudyVo> superclass = SwaggerStudyVo.class.getSuperclass();
        System.out.println("sc" + superclass.getName());
        Field[] fields1 = superclass.getDeclaredFields();
        for (Field field : fields1) {
            System.out.println(field.getName());
        }
//        System.out.println("=================");
//        Class<? super IpVo> aClass = IpVo.class.getSuperclass();
//        System.out.println(aClass == Object.class);
//        try {
//            //直接获取父类的属性，报错
//            Field pageNo = SwaggerStudyVo.class.getDeclaredField("pageNo");
//            ApiModelProperty annotation = pageNo.getAnnotation(ApiModelProperty.class);
//            System.out.println(annotation);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
    }

}

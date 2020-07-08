package com.der.dertool.constants;

import com.google.common.collect.Lists;
import org.springframework.http.MediaType;

import java.util.stream.Collectors;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-08 16:20
 */
public class StaticConstant {

    /**
     * 常用分隔符 后面有空白符使用时可新建其他常量或者trim
     */
    private static final String SEPARATOR = ", ";

    /**
     * 请求类型的string联合
     */
    public static final String CONTENT_TYPES_STRING = Lists.newArrayList(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE).stream().collect(Collectors.joining(SEPARATOR));

}

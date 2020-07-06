package com.der.dertool.enums;

import lombok.Getter;

/**
 * @program: der-tool
 * @description: 状态码
 * @author: long
 * @create: 2020-07-06 10:01
 */
public enum StatusCode {
    /*  调用成功 */
    SUCCESS(200, "调用成功"),
    /*  调用失败 */
    FAIL(500, "调用失败"),
    ;

    /**
     * 反应码
     */
    @Getter
    private Integer code;

    /**
     * 返回信息
     */
    @Getter
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

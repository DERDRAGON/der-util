package com.der.dertool.constants;

import com.der.dertool.enums.StatusCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: der-tool
 * @description: 返回结果
 * @author: long
 * @create: 2020-07-06 09:59
 */
@Data
@ApiModel(value = "返回结果",description = "这是一个总的返回结果")
public class DerResponse<T> {

    /**
     * 成功
     * @return success response
     */
    public static DerResponse openSuccess() {
        return new DerResponse(StatusCode.SUCCESS.getCode());
    }

    /**
     * 成功
     * @param t 返回结果
     * @param <T> 泛型
     * @return 返回结果
     */
    public static<T> DerResponse<T> openSuccess(T t) {
        return new DerResponse<T>(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMsg(), t);
    }

    /**
     * 失败
     * @return 返回结果
     */
    public static DerResponse openFail() {
        return new DerResponse(StatusCode.FAIL.getCode());
    }

    /**
     * 失败
     * @param statusCode 失败码及信息
     * @return 返回结果
     */
    public static DerResponse openFail(StatusCode statusCode) {
        return new DerResponse(statusCode.getCode(), statusCode.getMsg());
    }

    /**
     * 返回码
     */
    @ApiModelProperty(value = "返回码", notes = "返回状态码StatusCode里的code", example = "200", position = 1)
    private Integer code;

    /**
     * 返回信息
     */
    @ApiModelProperty(value = "返回信息", notes = "返回状态码StatusCode里的msg，或者自定义", example = "这是一个成功的返回信息", position = 2)
    private String msg;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据", notes = "成功的返回数据", position = 3)
    private T data;

    public DerResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public DerResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public DerResponse(Integer code) {
        this.code = code;
    }
}

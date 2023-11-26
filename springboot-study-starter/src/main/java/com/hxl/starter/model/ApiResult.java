package com.hxl.starter.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @Author hxl
 * @description
 * @Date 2023-11-13 13:25
 **/
@Getter
@Setter
@Accessors(chain = true)
public class ApiResult<T> {

    /*** 响应成功码 */
    private Boolean success;
    private String code;
    private String msg;
    private String traceId;
    private T data;

    public static <T> ApiResult<T> success() {
        ApiResult<T> apiResult = new ApiResult<T>();
        apiResult.setCode("200");
        apiResult.setSuccess(true);
        return apiResult;
    }

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> response = success();
        response.setData(data);
        return response;
    }
}

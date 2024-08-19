package com.example.alipaydemo.base.common;


import com.example.alipaydemo.base.enums.ResultEnum;
import lombok.Data;

/**
 * description:  结果信息
 * @author finding
 * @date 2024/03/28
 */
@Data
public class Result<T> {

    private String code;

    private String message;

    private static final String successCode = ResultEnum.SUCCESS.getCode();

    private static final String PARAM_ERROR_CODE = ResultEnum.PARAM_ERROR.getCode();

    private static final String SYSTEM_ERROR_CODE = ResultEnum.SYSTEM_ERROR.getCode();

    private T data;

    private Result() {
    }

    public static<T> Result<T> ok() {
        Result<T> result = new Result<>();
        result.setCode(successCode);
        result.setMessage(ResultEnum.SUCCESS.getValue());
        return result;
    }

    public static<T> Result<T> ok(String message) {
        Result<T> result = new Result<>();
        result.setCode(successCode);
        result.setMessage(message);
        return result;
    }

    public static<T> Result<T> ok(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(successCode);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static<T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setCode(successCode);
        result.setMessage("成功");
        result.setData(data);
        return result;
    }

    public static<T> Result<T> error(ResultEnum resultEnum) {
        Result<T> result = new Result<>();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getValue());
        return result;
    }

    public static<T> Result<T> error(ResultEnum resultEnum, T data) {
        Result<T> result = new Result<>();
        result.setCode(resultEnum.getCode());
        result.setMessage(resultEnum.getValue());
        result.setData(data);
        return result;
    }

    public static<T> Result<T> error(String code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static<T> Result<T> error(String code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}

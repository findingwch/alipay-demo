package com.example.alipaydemo.base.exception;


import com.example.alipaydemo.base.enums.ResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义错误
 *
 * @author finding
 * @date 2024/04/02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomizeException extends RuntimeException {

    private static final long serialVersionUID = 7476402684116590441L;

    private String code;

    private String msg;

    public CustomizeException(ResultEnum resultEnum) {
        super(resultEnum.getValue());
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getValue();
    }
}

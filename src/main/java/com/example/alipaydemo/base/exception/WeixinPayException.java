package com.example.alipaydemo.base.exception;

/**
 * 支付异常类
 *
 * @author Ray
 */
public class WeixinPayException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public WeixinPayException() {
        super("支付异常，请联系管理员");
    }

    public WeixinPayException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

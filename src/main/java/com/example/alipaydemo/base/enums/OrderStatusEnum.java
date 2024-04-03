package com.example.alipaydemo.base.enums;

import lombok.Getter;
import lombok.Setter;


/**
 * 订单状态枚举
 *
 * @author finding
 * @date 2024/04/02
 */
@Getter
public enum OrderStatusEnum {

    CREATE_NEW(0, "已下单"),
    PAYED(1, "已付款"),
    SENDED(2, "已发货"),
    RECIEVED(3, "已完成"),
    CANCLED(4, "已取消"),
    SERVICING(5, "售后中"),
    SERVICED(6, "售后完成");

    @Setter
    private Integer code;
    @Setter
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

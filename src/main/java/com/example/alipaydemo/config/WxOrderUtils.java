package com.example.alipaydemo.config;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * WX 订单实用程序
 *
 * @author finding
 * @date 2024/04/18
 */
@Slf4j
public class WxOrderUtils {

    public static final String ORDER_PAY_PREFIX = "ray_pay_";
    public static final String ORDER_REFUND_PREFIX = "ray_refund_";
    public static final String FORMAT = "yyyyMMddHHmmssSSS";

    /**
     * 生成商户订单号
     *
     * @return
     */
    public static String genOutTradeNo() {
        String order = ORDER_PAY_PREFIX + new SimpleDateFormat(FORMAT).format(new Date());
        log.debug("生成商户订单号: [{}]", order);
        return order;
    }

    /**
     * 生成退款订单号
     *
     * @return
     */
    public static String genRefundOrder() {
        String order = ORDER_REFUND_PREFIX + new SimpleDateFormat(FORMAT).format(new Date());
        log.debug("生成退款订单号: [{}]", order);
        return order;
    }
}


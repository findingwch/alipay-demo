package com.example.alipaydemo.vo;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 支付包异步通知响应的数据
 *
 * @author finding
 * @date 2024/04/02
 */
@Data
public class PayAsyncVo {

    private String gmt_create;
    /**
     * 字符集
     */
    private String charset;

    private String gmt_payment;
    /**
     * 通知时间
     */
    private LocalDateTime notify_time;
    /**
     * 主题
     */
    private String subject;
    /**
     * 标志
     */
    private String sign;
    /**
     * 买家 ID
     */
    private String buyer_id;//支付者的id
    /**
     * 订单的信息
     */
    private String body;//
    /**
     * 支付金额
     */
    private String invoice_amount;
    /**
     * 版本
     */
    private String version;
    /**
     * 通知 ID
     */
    private String notify_id;

    private String fund_bill_list;
    /**
     * 通知类型； trade_status_sync
     */
    private String notify_type;
    /**
     * 订单号
     */
    private String out_trade_no;
    /**
     * 支付的总额
     */
    private String total_amount;
    /**
     * 交易状态  TRADE_SUCCESS
     */
    private String trade_status;
    /**
     * 流水号
     */
    private String trade_no;

    private String auth_app_id;
    /**
     * 商家收到的款
     */
    private String receipt_amount;
    private String point_amount;
    /**
     * 应用 ID
     */
    private String app_id;
    /**
     * 最终支付的金额
     */
    private String buyer_pay_amount;

    /**
     * 签名类型
     */
    private String sign_type;
    /**
     * 商家id
     */
    private String seller_id;
}

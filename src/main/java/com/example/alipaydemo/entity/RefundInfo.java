package com.example.alipaydemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 退款信息
    */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "refund_info")
public class RefundInfo {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 退款的订单
     */
    @TableField(value = "order_return_id")
    private Long orderReturnId;

    /**
     * 退款金额
     */
    @TableField(value = "refund")
    private BigDecimal refund;

    /**
     * 退款交易流水号
     */
    @TableField(value = "refund_sn")
    private String refundSn;

    /**
     * 退款状态
     */
    @TableField(value = "refund_status")
    private Boolean refundStatus;

    /**
     * 退款渠道[1-支付宝，2-微信，3-银联，4-汇款]
     */
    @TableField(value = "refund_channel")
    private Integer refundChannel;

    @TableField(value = "refund_content")
    private String refundContent;
}
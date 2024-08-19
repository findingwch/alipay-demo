package com.example.alipaydemo.controller;


import com.example.alipaydemo.base.common.Result;
import com.example.alipaydemo.base.enums.ResultEnum;
import com.example.alipaydemo.base.exception.WeixinPayException;
import com.example.alipaydemo.config.WxOrderUtils;
import com.example.alipaydemo.config.WxPayResult;
import com.example.alipaydemo.service.WxPayRemoteService;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;


/**
 * wx pay 佯装
 *
 * @author finding
 * @date 2024/04/18
 */
@RestController
@Slf4j
@RequestMapping("/feign/wxPay")
public class WxPayFeign {

    @Autowired
    private WxPayRemoteService wxPayRemoteService;
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 生成商户订单号
     */
    @GetMapping("genOutTradeNo")
    public String genOutTradeNo() {
        return WxOrderUtils.genOutTradeNo();
    }

    /**
     * 生成退款订单
     */
    @GetMapping("genOutRefundNo")
    public String genOutRefundNo() {
        return WxOrderUtils.genRefundOrder();
    }

    /**
     * 调用统一下单接口
     */
    @GetMapping("createOrderV3")
    public Result<Map<String, Object>> createOrderV3() {
        try {
            log.debug("调用统一下单接口 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            // 先查询数据，如果存在并且二维码在有效期内，则直接返回（省略）

            // 调用下单API
            Map<String, Object> map = wxPayRemoteService.createOrderV3();
            // 商品订单号
            String outTradeNo = (String) map.get("outTradeNo");
            // 二维码
            String qrCode = (String) map.get("qrCode");

            // 保存数据（省略）

            // 返回数据
            return Result.ok(map);
        } catch (WxPayException e) {
            e.printStackTrace();
            throw new WeixinPayException(e.getErrCodeDes());
        }
    }


    @PostMapping("/native/notify")
    public WxPayResult nativeNotify(HttpServletRequest request) {
        return getWxPayResult(request);
    }

    private WxPayResult getWxPayResult(HttpServletRequest request) {
        log.debug("支付成功异步通知 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        if (lock.tryLock()) {
            try {
                // 解析支付结果v3通知
                WxPayOrderNotifyV3Result wxPayOrderNotifyV3Result = wxPayRemoteService.parseOrderNotifyV3Result(request);
                // 获取基本信息
                String tradeState = wxPayOrderNotifyV3Result.getResult().getTradeState();
                String tradeStateDesc = wxPayOrderNotifyV3Result.getResult().getTradeStateDesc();
                String outTradeNo = wxPayOrderNotifyV3Result.getResult().getOutTradeNo();

                // 获取用户支付订单信息（省略）

//                // 如果已经处理，直接返回成功标识
//                if (StringUtils.isNotBlank(user.getTradeState()) &&
//                        WxPayConstants.WxpayTradeStatus.SUCCESS.equals(user.getTradeState())) {
//                    // 返回成功标识
//                    return WxPayResult.ok();
//                }

                // 业务逻辑...（省略）

                // 返回成功标识
                return WxPayResult.ok();

            } catch (WxPayException e) {
                log.error(e.getMessage());
                // 返回失败标识
                return WxPayResult.error();
            } finally {
                lock.unlock();
            }
        }
        // 返回失败标识
        return WxPayResult.error();
    }


    @GetMapping("queryOrderV3/{outTradeNo}")
    public Result queryOrderV3(@PathVariable("outTradeNo") String outTradeNo) {
        try {
            log.debug("调用查询订单接口 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            // 查询订单
            WxPayOrderQueryV3Result wxPayOrderQueryV3Result = wxPayRemoteService.queryOrderV3("", outTradeNo);

            // 保存数据（省略）

            // 返回数据
            HashMap<String, Object> map = new HashMap<>();
            map.put("tradeState", wxPayOrderQueryV3Result.getTradeState());
            map.put("tradeStateDesc", wxPayOrderQueryV3Result.getTradeStateDesc());
            return Result.ok(map);
        } catch (WxPayException e) {
            e.printStackTrace();
            throw new WeixinPayException(e.getErrCodeDes());
        }
    }




}


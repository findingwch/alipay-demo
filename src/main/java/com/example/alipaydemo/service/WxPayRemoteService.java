package com.example.alipaydemo.service;

import com.example.alipaydemo.config.HtmlUtils;
import com.example.alipaydemo.config.WxOrderUtils;
import com.example.alipaydemo.config.WxPayPropertiesV3;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: WxPayRemoteService
 * @Description: 微信支付远程服务
 * @Author: Ray
 * @Date: 2022-02-10 14:09
 */
@Service
@Slf4j
public class WxPayRemoteService {

    @Autowired
    private WxPayService wxService;
    @Autowired
    private WxPayPropertiesV3 wxPayPropertiesV3;

    /**
     * @Description: 调用下单API
     * @Author: Ray
     * @Date: 2022-02-10 14:54
     **/
    public Map<String, Object> createOrderV3() throws WxPayException {
        log.debug("创建订单 >>>>> ");
		// 商品描述
        String description = "自定义商品描述";
        // 商户订单号
        String outTradeNo = WxOrderUtils.genOutTradeNo();
        // 总金额，单位分
        Integer total = 1;
        
        WxPayUnifiedOrderV3Request request = this.genWxPayUnifiedOrderV3Request(description, outTradeNo, total);
        String qrCode = wxService.createOrderV3(TradeTypeEnum.NATIVE, request);

        // 返回需要的信息
        HashMap<String, Object> map = new HashMap<>();;
        map.put("qrCode", qrCode);
        map.put("outTradeNo", outTradeNo);
        return map;
    }

    /**
     * @Description: 组装请求数据
     * @Author: Ray
     * @Date: 2022-02-10 14:30
     **/
    private WxPayUnifiedOrderV3Request genWxPayUnifiedOrderV3Request(String description, String outTradeNo, Integer total) {
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        // 商品描述
        request.setDescription(description);
        // 商户订单号
        request.setOutTradeNo(outTradeNo);

        // 通知地址
        request.setNotifyUrl(wxPayPropertiesV3.getNotifyUrl());
        // 订单金额
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        // 总金额，单位分
        amount.setTotal(total);
        // 货币类型，选填
        amount.setCurrency("CNY");
        request.setAmount(amount);
        return request;
    }


    public WxPayOrderNotifyV3Result parseOrderNotifyV3Result(HttpServletRequest request) throws WxPayException {
        // 解析支付结果v3通知
        return wxService.parseOrderNotifyV3Result(
                HtmlUtils.fetchRequest2Str(request),
                HtmlUtils.fetchRequest2SignatureHeader(request)
        );
    }

        public WxPayOrderQueryV3Result queryOrderV3(String transactionId, String outTradeNo) throws WxPayException {
        WxPayOrderQueryV3Result wxPayOrderQueryV3Result = this.wxService.queryOrderV3(transactionId, outTradeNo);
        log.debug("查询订单结果 >>>>> " + wxPayOrderQueryV3Result);
        return wxPayOrderQueryV3Result;
    }
}

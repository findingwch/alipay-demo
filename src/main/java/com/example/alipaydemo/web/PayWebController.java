package com.example.alipaydemo.web;

import com.alipay.api.AlipayApiException;
import com.example.alipaydemo.config.AlipayTemplate;
import com.example.alipaydemo.service.OrderService;
import com.example.alipaydemo.vo.PayVo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 支付宝支付controller接口
 *
 * @author Finding
 * @date 2022/3/23
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class PayWebController {

    private final AlipayTemplate alipayTemplate;

    private final OrderService orderService;


    /**
     * 支付接口：
     * 用户下单：支付宝支付
     * 1、让支付页让浏览器展示
     * 2、支付成功以后，跳转到用户的订单列表
     * produces:设置返回类型这里直接使用html格式支付宝的pay方法就是返回一个页面给我们使用
     *
     * @param orderSn 订单号
     * @return String
     * @throws AlipayApiException
     */
    @GetMapping(value = "/aliPayOrder", produces = "text/html")
    @ResponseBody
    public String aliPayOrder(@RequestParam("orderSn") String orderSn) throws AlipayApiException {

        PayVo payVo = orderService.getOrderPay(orderSn);
        return alipayTemplate.pay(payVo);

    }



}

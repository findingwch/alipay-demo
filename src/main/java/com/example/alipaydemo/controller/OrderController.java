package com.example.alipaydemo.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.alipaydemo.base.common.Result;
import com.example.alipaydemo.base.enums.ResultEnum;
import com.example.alipaydemo.base.exception.CustomizeException;
import com.example.alipaydemo.config.AlipayTemplate;
import com.example.alipaydemo.entity.Goods;
import com.example.alipaydemo.entity.Order;
import com.example.alipaydemo.entity.PaymentInfo;
import com.example.alipaydemo.mapper.PaymentInfoMapper;
import com.example.alipaydemo.service.GoodsService;
import com.example.alipaydemo.service.OrderService;
import com.example.alipaydemo.service.PaymentInfoService;
import com.example.alipaydemo.vo.PayVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 订单控制器
 *
 * @author Finding
 * @date 4/2/24
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("order")
public class OrderController {


    private final OrderService orderService;

    private final PaymentInfoService paymentInfoService;

    private final AlipayTemplate alipayTemplate;

    /**
     * 创建订单
     *
     * @param goodsId 商品编号
     * @param num     数量
     * @return {@link Result}
     */
    @PostMapping("/{goodsId}/{num}")
    public Result createOrder(@PathVariable Integer goodsId, @PathVariable Integer num) {

        orderService.createOrderWithGoods(goodsId,num);

        return Result.success();
    }



    /**
     * 获取订单列表
     *
     * @param pageNum  页码
     * @param pageSize 页面大小
     * @param order    条件
     * @return {@link Result}
     */
    @GetMapping("list")
    public Result getOrderList(@RequestParam(value = "pageNum", required = false) Long pageNum,
                               @RequestParam(value = "pageSize", required = false) Long pageSize,
                               @RequestBody(required = false) Order order){

        return Result.success(orderService.getPage(pageNum,pageSize,order));
    }


    /**
     * 支付宝退款接口
     *
     * @param orderSn 订购 SN
     * @return {@link Result}
     */
    @GetMapping("alipay/return")
    public Result returnPay(@RequestParam String orderSn) {

        Order orders = orderService.getOrderByOrderSn(orderSn);
        if (orders != null) {
            // 判断时间间隔
            long between = LocalDateTimeUtil.between(orders.getPaymentTime(), LocalDateTime.now(), ChronoUnit.DAYS);
            // 7天无理由退款
            if (between > 7) {
                return Result.error("500", "该订单已超过7天，不支持退款");
            }

            PaymentInfo paymentInfo  = paymentInfoService.getPaymentInfoByOrderSn(orderSn);
            try {
                //发送退款请求
                alipayTemplate.returnPay(paymentInfo);
                //修改订单相关状态
                orderService.updateOrderPayStatus(paymentInfo);
            } catch (CustomizeException e) {
                log.error(e.getMessage(), e);
                return Result.error(ResultEnum.SYSTEM_ERROR);
            }
        }

        return Result.success();
    }


}

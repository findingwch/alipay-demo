package com.example.alipaydemo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.alipaydemo.base.constants.PayContact;
import com.example.alipaydemo.base.enums.OrderStatusEnum;
import com.example.alipaydemo.base.enums.ResultEnum;
import com.example.alipaydemo.base.exception.CustomizeException;
import com.example.alipaydemo.entity.Goods;
import com.example.alipaydemo.entity.PaymentInfo;
import com.example.alipaydemo.entity.RefundInfo;
import com.example.alipaydemo.service.*;
import com.example.alipaydemo.base.utils.PageUtils;
import com.example.alipaydemo.base.utils.Query;
import com.example.alipaydemo.vo.PayAsyncVo;
import com.example.alipaydemo.vo.PayVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.alipaydemo.entity.Order;
import com.example.alipaydemo.mapper.OrderMapper;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/**
 * 订购服务 impl
 *
 * @author finding
 * @date 2024/04/02
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IService<Order>, OrderService {


    private final PaymentInfoService paymentInfoService;

    private final RefundInfoService refundInfoService;

    private final GoodsService goodsService;

    private final MQProducerService mqProducerService;

    public static final BigDecimal freigh = BigDecimal.valueOf(8);

    @Override
    public Order getOrderByOrderSn(String orderSn) {
        return this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderSn, orderSn));
    }

    @Override
    public void closeOrder(Order entity) {
        entity.setStatus(4);
        entity.setNote("订单已关闭");
        this.updateById(entity);
    }

    @Override
    public PayVo getOrderPay(String orderSn) {
        Order orderInfo = getOrderByOrderSn(orderSn);
        assert orderInfo != null : "订单号为空";
        PayVo payVo = new PayVo();

        payVo.setOut_trade_no(orderSn);

        //保留两位以上的小数点
        BigDecimal payAmount = orderInfo.getPayAmount().setScale(2, RoundingMode.UP);

        payVo.setTotal_amount(payAmount.toString());
        //设置商品描述 （可选）
        // payVo.setBody();
        payVo.setSubject(orderInfo.getUserName());
        return payVo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String handlePayResult(PayAsyncVo asyncVo) {

        Long id = this.getOrderByOrderSn(asyncVo.getOut_trade_no()).getId();

        //保存交易流水信息
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderSn(asyncVo.getOut_trade_no());
        paymentInfo.setOrderId(id);
        //支付宝交易流水号
        paymentInfo.setAlipayTradeNo(asyncVo.getTrade_no());
        paymentInfo.setTotalAmount(new BigDecimal(asyncVo.getBuyer_pay_amount()));
        paymentInfo.setSubject(asyncVo.getBody());
        paymentInfo.setPaymentStatus(asyncVo.getTrade_status());
        paymentInfo.setCreateTime(LocalDateTime.now());
        paymentInfo.setCallbackTime(asyncVo.getNotify_time());

        //添加到数据库中
        paymentInfoService.save(paymentInfo);

        String trade_status = asyncVo.getTrade_status();
        if ("TRADE_SUCCESS".equalsIgnoreCase(trade_status) || "TRADE_SUCCESS".equalsIgnoreCase(trade_status)) {
            //支付成功状态
            String orderSn = asyncVo.getOut_trade_no();//设置订单号
            this.updateOrderStatus(orderSn, OrderStatusEnum.PAYED.getCode(), PayContact.ALIPAY);
        }
        return "success";
    }

    @Override
    public PageUtils getPage(Long pageNum, Long pageSize, Order order) {

        IPage<Order> page = this.page(
                new Query<Order>().getPage(pageNum, pageSize),
                new QueryWrapper<Order>().allEq(BeanUtil.beanToMap(order), false));

        return new PageUtils(page);
    }

    @Override
    public void updateOrderStatus(String orderSn, Integer code, Integer payType) {
        Order orderEntity = new Order();
        orderEntity.setModifyTime(LocalDateTime.now());
        orderEntity.setStatus(code);
        orderEntity.setPaymentTime(LocalDateTime.now());
        orderEntity.setPayType(payType);
        this.baseMapper.update(orderEntity,
                new QueryWrapper<Order>().lambda().eq(Order::getOrderSn, orderSn));
    }

    /**
     * 更新订单支付状态
     *
     * @param paymentInfo 付款信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderPayStatus(PaymentInfo paymentInfo) {


        Order orderByOrderSn = this.getOrderByOrderSn(paymentInfo.getOrderSn());
        orderByOrderSn.setStatus(4);
        orderByOrderSn.setNote("已退款");


        RefundInfo refundInfo = RefundInfo.builder()
                .orderReturnId(orderByOrderSn.getId())
                .refund(paymentInfo.getTotalAmount())
                .refundSn(paymentInfo.getAlipayTradeNo())
                .refundChannel(1)
                .refundStatus(true)
                .build();
        refundInfoService.save(refundInfo);

        paymentInfoService.update(paymentInfo,
                new LambdaUpdateWrapper<PaymentInfo>()
                        .eq(PaymentInfo::getOrderSn, paymentInfo.getOrderSn()));

        this.updateById(orderByOrderSn);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrderWithGoods(Integer goodsId, Integer num) {
        Goods goods = goodsService.getById(goodsId);
        if (goods == null) {
            throw new CustomizeException(ResultEnum.GOODS_NOT_EXISTS);
        }
        if (goods.getStoreNum() < num) {
            throw new CustomizeException(ResultEnum.GOODS_NUM_ERROR);
        }
        // TODO: 4/2/24 获取当前用户
//        User currentUser = TokenUtils.getCurrentUser();
//        Integer userId = currentUser.getId();
        //创建订单信息
        BigDecimal totalPrice = goods.getPrice().multiply(BigDecimal.valueOf(num));

        //运费默认8
        BigDecimal payAmount = totalPrice.add(freigh);

        Order order = Order.builder()
                .orderSn(IdUtil.fastSimpleUUID())
                .totalAmount(totalPrice)
                .freightAmount(freigh)
                .userId(123L)
                .payAmount(payAmount)
                .build();

        this.save(order);
        //普通订单创建后减库存
        goods.setStoreNum(goods.getStoreNum() - num);
        goodsService.updateById(goods);
        //todo 发送延迟消息队列30分钟后如果没支付关闭订单回扣库存
        mqProducerService.sendDelayMsg(order,16);

    }
}

package com.example.alipaydemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.alipaydemo.entity.Order;
import com.example.alipaydemo.base.utils.PageUtils;
import com.example.alipaydemo.entity.PaymentInfo;
import com.example.alipaydemo.vo.PayAsyncVo;
import com.example.alipaydemo.vo.PayVo;

/**
 * @author Finding
 * @date 4/2/24
 */
public interface OrderService extends IService<Order> {

    /**
     * 根据订单号查询订单信息
     *
     * @param orderSn 订购 SN
     * @return {@link Order}
     */
    Order getOrderByOrderSn(String orderSn);

    /**
     * 关闭订单
     *
     * @param entity 实体
     */
    void closeOrder(Order entity);

    /**
     * 通过订单号返回出pay支付需要的数据
     *
     * @param orderSn 订购id
     * @return {@link PayVo}
     */
    PayVo getOrderPay(String orderSn);

    /**
     * 支付成功修改订单状态
     *
     * @param asyncVo 异步 VO
     * @return {@link String}
     */
    String handlePayResult(PayAsyncVo asyncVo);


    /**
     * 获取页面
     *
     * @param pageNum  页码
     * @param pageSize 页面大小
     * @param order    次序
     * @return {@link PageUtils}
     */
    PageUtils getPage(Long pageNum, Long pageSize, Order order);

    /**
     * 更新订单状态
     *
     * @param orderSn 订购 SN
     * @param code    法典
     * @param payType 薪酬类型
     */
    void updateOrderStatus(String orderSn, Integer code, Integer payType);


    /**
     * 更新订单支付状态
     *
     * @param paymentInfo 付款信息
     */
    void updateOrderPayStatus(PaymentInfo paymentInfo);

    /**
     * 使用商品创建订单
     *
     * @param goodsId 商品编号
     * @param num     数量
     */
    void createOrderWithGoods(Integer goodsId, Integer num);
}

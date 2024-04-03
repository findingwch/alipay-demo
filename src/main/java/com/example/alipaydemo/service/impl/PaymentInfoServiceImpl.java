package com.example.alipaydemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.alipaydemo.mapper.PaymentInfoMapper;
import com.example.alipaydemo.entity.PaymentInfo;
import com.example.alipaydemo.service.PaymentInfoService;
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService{

    @Override
    public PaymentInfo getPaymentInfoByOrderSn(String orderSn) {

        return this.getOne(new LambdaQueryWrapper<PaymentInfo>().eq(PaymentInfo::getOrderSn,orderSn));
    }
}

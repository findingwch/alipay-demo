package com.example.alipaydemo.service;

import com.example.alipaydemo.entity.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;
public interface PaymentInfoService extends IService<PaymentInfo>{


    PaymentInfo getPaymentInfoByOrderSn(String orderSn);

}

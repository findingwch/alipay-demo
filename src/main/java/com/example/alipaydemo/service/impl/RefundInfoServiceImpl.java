package com.example.alipaydemo.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.alipaydemo.mapper.RefundInfoMapper;
import com.example.alipaydemo.entity.RefundInfo;
import com.example.alipaydemo.service.RefundInfoService;
@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService{

}

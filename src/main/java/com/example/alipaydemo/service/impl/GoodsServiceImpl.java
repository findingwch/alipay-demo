package com.example.alipaydemo.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.alipaydemo.mapper.GoodsMapper;
import com.example.alipaydemo.entity.Goods;
import com.example.alipaydemo.service.GoodsService;
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService{

}

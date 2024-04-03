package com.example.alipaydemo.controller;

import cn.hutool.core.util.IdUtil;
import com.example.alipaydemo.base.common.Result;
import com.example.alipaydemo.entity.Order;
import com.example.alipaydemo.service.MQProducerService;
import com.example.alipaydemo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.producer.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * rocketmqtest
 *
 * @author finding
 * @date 2024/04/03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/rocketmq")
public class RocketMQController {


    private final MQProducerService mqProducerService;

    private final OrderService orderService;

    /**
     * 发送
     */
    @GetMapping("/send")
    public void send() {
        Order order = Order.builder()
                .orderSn(IdUtil.fastSimpleUUID())
                .totalAmount(BigDecimal.valueOf(100))
                .freightAmount(BigDecimal.valueOf(8))
                .userId(123L)
                .payAmount(BigDecimal.valueOf(100))
                .build();
        mqProducerService.send(order);
    }

    /**
     * 发送带有标识标记的
     *
     * @return {@link Result}<{@link SendResult}>
     */
    @GetMapping("/sendTag")
    public Result<SendResult> sendTag() {
        SendResult sendResult = mqProducerService.sendTagMsg("带有tag的字符消息");
        return Result.success(sendResult);
    }

}

package com.example.alipaydemo.listen;

import com.alibaba.fastjson.JSON;
import com.example.alipaydemo.entity.Order;
import com.example.alipaydemo.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * MQPamenter服务
 *
 * @author finding
 * @date 2024/04/03
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MQConsumerService {

    private final OrderService orderService;

    // topic需要和生产者的topic一致，consumerGroup属性是必须指定的，内容可以随意
    // selectorExpression的意思指的就是tag，默认为“*”，不设置的话会监听所有消息
    @Service
    @RocketMQMessageListener(topic = "kingsTopic", selectorExpression = "tag1", consumerGroup = "springboot_consumer_group")
    public class ConsumerSend implements RocketMQListener<Order> {
        // 监听到消息就会执行此方法
        @Override
        public void onMessage(Order user) {
            log.info("监听到消息：order={}", JSON.toJSONString(user));
        }
    }



    // topic需要和生产者的topic一致，consumerGroup属性是必须指定的，内容可以随意
    // selectorExpression的意思指的就是tag，默认为“*”，不设置的话会监听所有消息
    @Service
    @RocketMQMessageListener(topic = "kingsTopic", selectorExpression = "tag3",consumerGroup = "springboot_consumer_group")
    public class ConsumerSend3 implements RocketMQListener<Order> {
        // 监听到消息就会执行此方法
        @Override
        public void onMessage(Order order) {
            log.info("监听到消息：order={}", JSON.toJSONString(order));
            //开始关闭订单
            orderService.closeOrder(order);
        }
    }


    // 注意：这个ConsumerSend2和上面ConsumerSend在没有添加tag做区分时，不能共存，
    // 不然生产者发送一条消息，这两个都会去消费，如果类型不同会有一个报错，所以实际运用中最好加上tag，写这只是让你看知道就行
    @Service
    @RocketMQMessageListener(topic = "kingsTopic", consumerGroup = "Con_Group_two")
    public class ConsumerSend2 implements RocketMQListener<String> {
        @Override
        public void onMessage(String str) {
            log.info("监听到消息：str={}", str);
        }
    }

	// MessageExt：是一个消息接收通配符，不管发送的是String还是对象，都可接收，当然也可以像上面明确指定类型（我建议还是指定类型较方便）
    @Service
    @RocketMQMessageListener(topic = "kingsTopic", selectorExpression = "tag2", consumerGroup = "Con_Group_Three")
    public class Consumer implements RocketMQListener<MessageExt> {
        @Override
        public void onMessage(MessageExt messageExt) {
            byte[] body = messageExt.getBody();
            String msg = new String(body);
            log.info("监听到消息：msg={}", msg);
        }
    }

}

package com.ludens.springbootmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    //注入
    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }
    //交换机确认回调方法
    //1. 发消息，交换机接收到了，回调参数：
    //  ① correlationData 保存回调信息ID及相关信息
    //  ② 交换机收到消息 即为true
    //  ③ cause
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId(): "";
        if (ack){
            log.info("交换机收到了id为 {} 的消息",id);
        } else{
            log.info("交换机还未收到id为 {} 的消息，原因为 {}",id, cause);
        }
    }

    //可以再当消息传递过程中达不到目的地时将消息返回给生产者
    //不可达目的地时才会进行回退
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息{}被交换机{}退回， 退回原因：{}， 路由key：{}", new String(returnedMessage.getMessage().getBody()), returnedMessage.getExchange(),
                returnedMessage.getReplyText(), returnedMessage.getRoutingKey());
    }
}

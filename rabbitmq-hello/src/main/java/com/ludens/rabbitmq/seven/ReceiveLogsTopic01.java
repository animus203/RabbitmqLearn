package com.ludens.rabbitmq.seven;


import com.ludens.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 声明主题交换机 及相关队列
 *
 * 消费者c1
 */
public class ReceiveLogsTopic01 {
    public static final String EXCHANGE_NAME = "topic_logs";

    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //声明队列
        String queueName  = "Q1";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("等待接收消息……");
        //先写一个接收消息的DeliverCallback
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("接收队列："+ queueName + "绑定键："+ message.getEnvelope().getRoutingKey());
            System.out.println("01控制台打印接收到的消息："+ new String(message.getBody()));
        };
        //接收消息
        channel.basicConsume(queueName, true, deliverCallback, consumerTag ->{});
    }
}

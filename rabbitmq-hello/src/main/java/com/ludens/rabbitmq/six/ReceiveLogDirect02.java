package com.ludens.rabbitmq.six;

import com.ludens.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogDirect02 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个队列
        channel.queueDeclare("disk", false, false, false, null);
        //绑定交换机与队列
        channel.queueBind("disk", EXCHANGE_NAME, "error");

        System.out.println("等待接收消息……");

        //先写一个接收消息的DeliverCallback
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("02控制台打印接收到的消息："+ new String(message.getBody()));
        };
        channel.basicConsume("disk", true, deliverCallback, consumerTag ->{});
    }
}

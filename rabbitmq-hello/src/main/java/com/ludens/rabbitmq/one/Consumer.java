package com.ludens.rabbitmq.one;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂ip 连接RabbitMQ的队列
        factory.setHost("192.168.202.128");
        //用户名 密码
        factory.setUsername("tsunami");
        factory.setPassword("575886");
        //创建连接
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        //声明 接收消息
        DeliverCallback deliverCallback = (consumerTag, message)-> {
            System.out.println(message.getBody());
        };

        //取消消息时的回调
        CancelCallback cancelCallback = comsumerTag ->{
            System.out.println("消息消费被中断");
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}

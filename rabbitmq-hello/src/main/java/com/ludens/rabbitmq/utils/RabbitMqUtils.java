package com.ludens.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 此类为连接工厂创建信道的工具类
 */
public class RabbitMqUtils {
    public static Channel getChannel() throws IOException, TimeoutException {
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
        return channel;
    }
}

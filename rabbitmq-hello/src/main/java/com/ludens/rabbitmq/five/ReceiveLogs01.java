package com.ludens.rabbitmq.five;

import com.ludens.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息接收
 */
public class ReceiveLogs01 {

    //交换机的名称
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //声明一个队列,临时队列
        //队列名称随机，当消费者断开队列连接时，队列自动删除
        String queueName = channel.queueDeclare().getQueue();
        //绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息……");

        //先写一个接收消息的DeliverCallback
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("01控制台打印接收到的消息："+ new String(message.getBody()));
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag ->{});
    }
}

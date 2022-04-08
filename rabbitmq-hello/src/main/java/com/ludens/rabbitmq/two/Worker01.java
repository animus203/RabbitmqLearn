package com.ludens.rabbitmq.two;

import com.ludens.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作线程：相当于消费者
 */
public class Worker01 {
    //队列名称
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();

        //声明 接收消息
        DeliverCallback deliverCallback = (consumerTag, message)-> {
            System.out.println("接收到的消息: "+ new String(message.getBody()));
        };

        //取消消息时的回调
        CancelCallback cancelCallback = comsumerTag ->{
            System.out.println(comsumerTag+" 消息消费被中断");
        };

        System.out.println("Worker2等待消息……");
        //接受消息
        channel.basicConsume(QUEUE_NAME, true,deliverCallback, cancelCallback);
    }
}

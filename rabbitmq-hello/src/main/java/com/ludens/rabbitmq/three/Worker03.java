package com.ludens.rabbitmq.three;

import com.ludens.rabbitmq.utils.RabbitMqUtils;
import com.ludens.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者手动应答时不丢失，放回队列中重新消费
 */
public class Worker03 {
    //队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        System.out.println("worker03等候消息，时间较短");

        //声明 接收消息
        DeliverCallback deliverCallback = (consumerTag, message)-> {
            //沉睡1s
            SleepUtils.sleep(1);
            System.out.println("接收到的消息："+ new String(message.getBody()));

            //手动应答
            //1. 消息的标记tag
            //2. 是否批量应答
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

        };
        //设置不公平分发
        channel.basicQos(1);
        boolean autoAck = false;

        //取消消息时的回调
        CancelCallback cancelCallback = comsumerTag ->{
            System.out.println("消息消费被中断");
        };
        //采用手动应答
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);

    }
}

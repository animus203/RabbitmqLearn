package com.ludens.rabbitmq.four;

import com.ludens.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认没事
 * 1. 单个确认
 * 2. 批量确认
 * 3. 异步批量确认
 */
public class ConfirmMessage {
    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        //1. 单个确认
        //ConfirmMessage.publishMessageIndivi();

        //2. 批量确认
        //ConfirmMessage.publishMessageMulti();

        //3. 异步确认
        ConfirmMessage.publishMessageAsync();
        


    }
    public static final int MESSAGE_COUNT = 1000;
    //1. 单个确认
    public static void publishMessageIndivi() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        
        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布1000条消耗时间为："+ (end - begin) + "ms");
    }

    //批量发布确认
    public static void publishMessageMulti() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            if (i%100 == 0) {
                //100条时批量确认一次
                channel.waitForConfirms();
                System.out.println("100条了，确认一次");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布1000条消耗时间为："+ (end - begin) + "ms");
    }

    //3. 异步发送确认
    public static void publishMessageAsync() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMqUtils.getChannel();
        //队列声明
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, false,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //消息成功的回调
        ConfirmCallback ackCallback = (deliveryTag, multiple)->{
            System.out.println("确认的消息：" + deliveryTag);
        };

        //消息失败的回调
        ConfirmCallback nackCallback = (deliveryTag, multiple)->{
            System.out.println("未确认的消息：" + deliveryTag);
        };
        //准备消息的监听器，监听消息是否成功
        channel.addConfirmListener(ackCallback, nackCallback);

        //批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

        }

        long end = System.currentTimeMillis();
        System.out.println("发布1000条消耗时间为："+ (end - begin) + "ms");
    }

}

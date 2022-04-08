package com.ludens.rabbitmq.seven;

import com.ludens.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {
    public static final String EXCHANGE_NAME = "topic_logs";

    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        Map<String, String> bindingKeyMap = new HashMap<>();
        bindingKeyMap.put("quick.orange.rabbit", "Q1Q2");
        bindingKeyMap.put("lazy.orange.elephant", "Q1Q2");
        bindingKeyMap.put("quick.orange.fox", "Q1");
        bindingKeyMap.put("lazy.brown.fox", "Q2");
        bindingKeyMap.put("quick.brown.fox", "null");
        bindingKeyMap.put("quick.orange.male.rabbit", "null");
        bindingKeyMap.put("lazy.orange.male.rabbit", "Q2");

        for (Map.Entry<String, String> stringStringEntry : bindingKeyMap.entrySet()) {
            String routingKey = stringStringEntry.getKey();
            String message = stringStringEntry.getValue();
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("生产者发送消息");
        }
    }
}

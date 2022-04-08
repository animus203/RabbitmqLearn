package com.ludens.springbootmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送延迟消息
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    //开始发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
        log.info("当前时间：{}，发送一条信息给两个TTL队列：{}", new Date().toString(), message);
        //发送消息
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列:"+message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列:"+message);
    }

    //开始发消息
    @GetMapping("/sendExpMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime){
        log.info("当前时间：{}，发送一条时长为{}ms的队列给QC：{}", new Date().toString(),ttlTime, message);
        //发送消息
        rabbitTemplate.convertAndSend("X", "XC", message, msg->{
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列:"+message);
    }




}

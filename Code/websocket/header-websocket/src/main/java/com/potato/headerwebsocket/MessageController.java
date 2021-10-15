package com.potato.headerwebsocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import java.security.Principal;

@Controller
public class MessageController {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    /**
     * 点对点发送消息，将消息发送到指定用户
     */
    @MessageMapping("/test")
    public void sendUserMessage(Principal principal, MessageBody messageBody) {
        // 设置发送消息的用户
//        messageBody.setFrom(principal.getName());
        // 调用 STOMP 代理进行消息转发
        simpMessageSendingOperations.convertAndSendToUser(messageBody.getTargetUser(), messageBody.getDestination(), messageBody);
    }

}
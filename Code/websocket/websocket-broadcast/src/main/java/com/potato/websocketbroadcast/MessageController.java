package com.potato.websocketbroadcast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * @MessageMapping(“/sendTest”)
 * 接收客户端发送的消息，当客户端发送消息的目的地为/app/sendTest时，交给该注解所在的方法处理消息，其中/app是在WebSocketConfig配置文件configureMessageBroker方法中添加:
 * registry.setApplicationDestinationPrefixes("/app");
 * 若没有添加@SendTo注解且该方法有返回值，则返回的目的地地址为/topic/sendTest，经过消息代理，客户端需要订阅了这个主题才能收到返回消息
 *
 * @SubscribeMapping 接收客户端发送的订阅，当客户端订阅的目的地为/app/subscribeTest时，交给该注解所在的方法处理订,
 * 其中/app为客户端请求前缀,若没有添加@SendTo注解且该方法有返回值，则返回的目的地地址为/app/sendTest，不经过消息代理，客户端需要订阅了这个主题才能收到返回消息
 *
 * @SendTo(“/topic/subscribeTest”)
 * 修改返回消息的目的地地址为/topic/subscribeTest，经过消息代理，客户端需要订阅了这个主题才能收到返回消息
 */
@Controller
public class MessageController {

    /** 消息发送工具对象 */
    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;


    /** 广播发送消息，将消息发送到指定的目标地址 */
    @MessageMapping("/test") //接收客户端请求
    public void sendTopicMessage(MessageBody messageBody) {
        // 将消息发送到 WebSocket 配置类中配置的代理中（/topic）进行消息转发
        simpMessageSendingOperations.convertAndSend(messageBody.getDestination(), messageBody);

    }

    @SendTo("topic/sub")
    public String test(){
        return "aaa";
    }

}
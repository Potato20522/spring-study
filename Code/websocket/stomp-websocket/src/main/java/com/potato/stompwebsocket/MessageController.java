package com.potato.stompwebsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
@Slf4j
public class MessageController {

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/marco") //隐含前缀:/app
    @SendTo("/topic/marco") //发向客户端的路径（要写全）,客户端需要订阅这个路径
    public Shout stompHandle(Shout shout){
        log.info("接收到消息：" + shout.getMessage());
        Shout s = new Shout();
        s.setMessage("Polo!");
        //返回值通过@send发出去，不写的@send或，@send没有指定路径，那么默认路径就是@MessageMapping注解的路径再加前缀：enableSimpleBroker设定的
        return s;
    }

    @SubscribeMapping("/getShout")
    public Shout getShout(){
        Shout shout = new Shout();
        shout.setMessage("Hello STOMP");
        return shout;//返回值不走代理，直接到客户端订阅方法的回调里
    }

    /**
     * 广播消息，不指定用户，所有订阅此的用户都能收到消息
     * @param shout
     */
    @MessageMapping("/broadcastShout")
    public void broadcast(Shout shout) {
        simpMessageSendingOperations.convertAndSend("/topic/shouts", shout);
    }

    @MessageMapping("/shout") //用户发送的路径：/app/shout
    @SendToUser("/queue/notifications") //用户订阅的路径/user/queue/notifications-'username'
    public Shout userStomp(Principal principal, Shout shout) {
        String name = principal.getName();
        String message = shout.getMessage();
        log.info("认证的名字是：{}，收到的消息是：{}", name, message);
        return shout;
    }

    @MessageMapping("/singleShout")
    public void singleUser(Shout shout, StompHeaderAccessor stompHeaderAccessor) {
        String message = shout.getMessage();
        log.info("接收到消息：" + message);
        Principal user = stompHeaderAccessor.getUser();

        simpMessageSendingOperations.convertAndSendToUser(user.getName(), "/queue/shouts", shout);
    }
}
package com.potato.securitywebsocket;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //设置心跳为10秒
    private static long HEART_BEAT=10000;
    /**
     * 配置 WebSocket 进入点，及开启使用 SockJS，这些配置主要用配置连接端点，用于 WebSocket 连接
     *
     * @param registry STOMP 端点
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/mydlq")
                .addInterceptors(new HttpSessionHandshakeInterceptor());
    }

    /**
     * 配置消息代理选项
     *
     * @param registry 消息代理注册配置
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //https://blog.csdn.net/cw_hello1/article/details/80885171 设置服务端心跳
        // 设置一个或者多个代理前缀，在 Controller 类中的方法里面发生的消息，会首先转发到代理从而发送到对应广播或者队列中。
        ThreadPoolTaskScheduler te = new ThreadPoolTaskScheduler();
        te.setPoolSize(1);
        te.initialize();
        te.setThreadNamePrefix("ws-heartbeat-thread-");
        registry.enableSimpleBroker("/queue")
                .setHeartbeatValue(new long[]{HEART_BEAT,HEART_BEAT}).setTaskScheduler(te);//或者传入 new DefaultManagedTaskScheduler()

        // 配置客户端发送请求消息的一个或多个前缀，该前缀会筛选消息目标转发到 Controller 类中注解对应的方法里
        registry.setApplicationDestinationPrefixes("/app");
        // 服务端通知特定用户客户端的前缀，可以不设置，默认为user
        registry.setUserDestinationPrefix("/user");
    }


    @Bean
    public DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler();
    }


}
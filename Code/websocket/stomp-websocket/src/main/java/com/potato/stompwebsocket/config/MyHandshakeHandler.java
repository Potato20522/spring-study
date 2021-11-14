package com.potato.stompwebsocket.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class MyHandshakeHandler extends DefaultHandshakeHandler {
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map attributes) throws Exception {

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest
                    = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest
                    .getServletRequest().getSession();
            attributes.put("sessionId", session.getId());
        }
        return true;
    }

}

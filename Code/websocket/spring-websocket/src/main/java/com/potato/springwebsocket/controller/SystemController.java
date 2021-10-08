package com.potato.springwebsocket.controller;

import cn.hutool.core.lang.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


@RestController
public class SystemController {


    //    // 推送数据到websocket客户端 接口
//    @GetMapping("/socket/push/{cid}")
//    public Map pushMessage(@PathVariable("cid") String cid, String message) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            HashSet<String> sids = new HashSet<>();
//            sids.add(cid);
//            WebSocketServer.sendMessage("服务端推送消息：" + message, sids);
//            result.put("code", cid);
//            result.put("msg", message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(String message) throws Exception {

        Thread.sleep(1000); // simulated delay
        return "服务端说:, " +message;
    }
}
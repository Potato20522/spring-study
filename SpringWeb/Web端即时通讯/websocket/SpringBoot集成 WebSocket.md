# javax的方式(原生)

[SpringBoot 集成 WebSocket 实现服务端消息主动推送_QiuHaoqian的博客-CSDN博客](https://blog.csdn.net/QiuHaoqian/article/details/117067841)

- javax.websocket包下的注解和类是**规范**

- SpringBoot自带的Tomcat**来现**了webSocket服务

在下面的代码中，我们在WebSocketServer中依赖的库只有javax下的注解和api，没有用到spring或tomcat中关于websocket的任何api,Springboot只是负责管理这些Bean而已

## 依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

## SpringBoot开启WebSocket支持

**WebSocketConfig.java**

通过配置类 启用WebSocket的支持：

```java
@Configuration
public class WebSocketConfig {
    //这个Bean会自动注册使用@ServerEndpoint注解声明的websocket endpoint
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
```

## 定义websocket服务器端

WebSocket是类似服务端的形式(采用ws协议)，那么这里的WebSocketServer其实就相当于一个ws协议的 服务器 或 Controller

@ ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端, 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端

**WebSocketServer.java**

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端。
 */
@Component
@Slf4j
@Service
@ServerEndpoint("/api/websocket/{sid}")
public class WebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    //concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //接收sid
    private String sid = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketSet.add(this);     // 加入set中
        this.sid = sid;
        addOnlineCount();           // 在线数加1
        try {
            sendMessage("conn_success");
            log.info("有新客户端开始监听,sid=" + sid + ",当前在线人数为:" + getOnlineCount());
        } catch (IOException e) {
            log.error("websocket IO Exception");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  // 从set中删除
        subOnlineCount();              // 在线数减1
        // 断开连接情况下，更新主板占用情况为释放
        log.info("释放的sid=" + sid + "的客户端");
        releaseResource();
    }

    private void releaseResource() {
        // 这里写释放资源和要处理的业务
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @Param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自客户端 sid=" + sid + " 的信息:" + message);
        // 群发消息
        HashSet<String> sids = new HashSet<>();
        for (WebSocketServer item : webSocketSet) {
            sids.add(item.sid);
        }
        try {
            sendMessage("客户端 " + this.sid + "发布消息：" + message, sids);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生错误回调
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error(session.getBasicRemote() + "客户端发生错误");
        error.printStackTrace();
    }

    /**
     * 群发自定义消息
     */
    public static void sendMessage(String message, HashSet<String> toSids) throws IOException {
        log.info("推送消息到客户端 " + toSids + "，推送内容:" + message);

        for (WebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给传入的sid，为null则全部推送
                if (toSids.size() <= 0) {
                    item.sendMessage(message);
                } else if (toSids.contains(item.sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    /**
     * 实现服务器主动推送消息到 指定客户端
     */
    public void sendMessage(String message) throws IOException {
        //这是发送的是文本信息(可以传JSON)，还可以有发送二进制信息：sendBinary()
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 获取当前在线人数
     *
     * @return
     */
    public static int getOnlineCount() {
        return onlineCount.get();
    }

    /**
     * 当前在线人数 +1
     *
     * @return
     */
    public static void addOnlineCount() {
        onlineCount.getAndIncrement();
    }

    /**
     * 当前在线人数 -1
     *
     * @return
     */
    public static void subOnlineCount() {
        onlineCount.getAndDecrement();
    }

    /**
     * 获取当前在线客户端对应的WebSocket对象
     *
     * @return
     */
    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }
}
```

## 服务端推送消息到客户端

这是一个用于测试**服务端主动推送消息到客户端**的测试接口，写不写controller无所谓，关键代码是这一句：

```java
WebSocketServer.sendMessage("服务端推送消息：" + message, sids);//可以在业务逻辑里调用
```

sendMessage()方法

**SystemController.java**

```java
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RestController
public class SystemController {

    // 推送数据到websocket客户端 接口
    @GetMapping("/socket/push/{cid}")
    public Map pushMessage(@PathVariable("cid") String cid, String message) {
        Map<String, Object> result = new HashMap<>();
        try {
            HashSet<String> sids = new HashSet<>();
            sids.add(cid);
            WebSocketServer.sendMessage("服务端推送消息：" + message, sids);
            result.put("code", cid);
            result.put("msg", message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
```

## 前端websocket客户端

这是一个简单的前端js编写的 websocket客户端 index.html 内容如下：

```html
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>Java后端WebSocket的Tomcat实现</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>

<body>
    <div id="message"></div>
    <hr />
    <div id="main"></div>
    <div id="client"></div>
    <input id="text" type="text" />
    <button onclick="send()">发送消息</button>
    <hr />
    <button onclick="closeWebSocket()">关闭WebSocket连接</button>
</body>
<script type="text/javascript">

    var cid = Math.floor(Math.random() * 100); // 随机生成客户端id
    document.getElementById('client').innerHTML += "客户端 id = " + cid + '<br/>';

    var websocket = null;

    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        // 改成你的地址
        websocket = new WebSocket("ws://127.0.0.1:8080/api/websocket/" + cid);
    } else {
        alert('当前浏览器 Not support websocket')
    }

    //连接发生错误的回调方法
    websocket.onerror = function () {
        setMessageInnerHTML("websocket.onerror: WebSocket连接发生错误");
    };

    //连接成功建立的回调方法
    websocket.onopen = function () {
        setMessageInnerHTML("websocket.onopen: WebSocket连接成功");
    }

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        setMessageInnerHTML("websocket.onmessage: " + event.data);
    }

    //连接关闭的回调方法
    websocket.onclose = function () {
        setMessageInnerHTML("websocket.onclose: WebSocket连接关闭");
    }

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }

    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
        alert('websocket.close: 关闭websocket连接')
    }

    //发送消息
    function send() {
        var message = document.getElementById('text').value;
        try {
            websocket.send('{"msg":"' + message + '"}');
            setMessageInnerHTML("websocket.send: " + message);
        } catch (err) {
            console.error("websocket.send: " + message + " 失败");
        }
    }
</script>

</html>

```

## 小结

这种方式虽然有@ServerEndpoint、@OnOpen、@OnClose之类的注解可以使用，但是还是需要写一个WebSocketServer类，里面要写很多的方法，有点繁琐，~~而且在调用服务端发给客户端WebSocketServer.sendMessage()这个方法时，需要在业务代码里主动去调用这个方法，有点耦合~~

# 一些配置api

## WebSocketSession

WebSocketSession 的增删查,  这里简单通过 ConcurrentHashMap 来实现了一个 session 池，用来保存已经登录的 websocket的session。服务端发送消息给客户端必须要通过这个 **session**。

```java
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WsSessionManager {
    /**
     * 保存连接 session 的地方
     */
    private static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 添加 session
     */
    public static void add(String key, WebSocketSession session) {
        // 添加 session
        SESSION_POOL.put(key, session);
    }

    /**
     * 删除 session,会返回删除的 session
     */
    public static WebSocketSession remove(String key) {
        // 删除 session
        return SESSION_POOL.remove(key);
    }

    /**
     * 删除并同步关闭连接
     */
    public static void removeAndClose(String key) {
        WebSocketSession session = remove(key);
        if (session != null) {
            try {
                // 关闭连接
                session.close();
            } catch (IOException e) {
                // todo: 关闭出现异常处理
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得 session
     */
    public static WebSocketSession get(String key) {
        // 获得 session
        return SESSION_POOL.get(key);
    }
}
```

## WebSocketHandler

如题,就是对WebSocket中建立连接、接收消息、断开连接等事件做处理

有处理文本的:TextWebSocketHandler,也有处理二进制的BinaryWebSocketHandler

**HttpAuthHandler.java**

```java
@Component
public class HttpAuthHandler extends TextWebSocketHandler {

    /**
     * socket 建立成功事件
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Object token = session.getAttributes().get("token");
        if (token != null) {
            // 用户连接成功，放入在线用户缓存
            WsSessionManager.add(token.toString(), session);
        } else {
            throw new RuntimeException("用户登录已经失效!");
        }
    }

    /**
     * 接收消息事件
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 获得客户端传来的消息
        String payload = message.getPayload();
        Object token = session.getAttributes().get("token");
        System.out.println("server 接收到 " + token + " 发送的 " + payload);
        session.sendMessage(new TextMessage("server 发送给 " + token + " 消息 " + payload + " " + LocalDateTime.now().toString()));
    }

    /**
     * socket 断开连接时
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Object token = session.getAttributes().get("token");
        if (token != null) {
            // 用户退出，移除缓存
            WsSessionManager.remove(token.toString());
        }
    }
}
```

## HandshakeInterceptor

顾名思义，握手前后的一些逻辑。

通过实现 **HandshakeInterceptor** 接口来定义握手拦截器，注意这里与上面 **Handler** 的事件是不同的，这里是建立握手时的事件，分为握手前与握手后，而   **Handler** 的事件是在握手成功后的基础上建立 socket 的连接。所以在如果把认证放在这个步骤相对来说最节省服务器资源。它主要有两个方法  **beforeHandshake** 与  **afterHandshake **，顾名思义一个在握手前触发，一个在握手后触发。

```java
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 *
 */
@Component
public class MyInterceptor implements HandshakeInterceptor {

    /**
     * 握手前
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        System.out.println("握手开始");
        // 获得请求参数
        Map<String, String> paramMap = HttpUtil.decodeParamMap(request.getURI().getQuery(), StandardCharsets.UTF_8);
        String uid = paramMap.get("token");
        if (StrUtil.isNotBlank(uid)) {
            // 放入属性域
            attributes.put("token", uid);
            System.out.println("用户 token " + uid + " 握手成功！");
            return true;
        }
        System.out.println("用户登录已失效");
        return false;
    }

    /**
     * 握手后
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        System.out.println("握手完成");
    }

}
```

## WebSocketConfigurer 

通过实现  **WebSocketConfigurer** 类并覆盖相应的方法进行 **websocket** 的配置。我们主要覆盖  **registerWebSocketHandlers** 这个方法。通过向  **WebSocketHandlerRegistry** 设置不同参数来进行配置。其中 **addHandler **方法添加我们上面的写的 ws 的   handler 处理类，第二个参数是你暴露出的 ws 路径。**addInterceptors **添加我们写的握手过滤器。**setAllowedOrigins("*") **这个是关闭跨域校验，方便本地调试，线上推荐打开。

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private HttpAuthHandler httpAuthHandler;
    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(httpAuthHandler, "myWS")//添加建立断开连接处理器
                .addInterceptors(myInterceptor)//添加握手拦截器
                .setAllowedOrigins("*");//关闭跨域校验
    }
}
```

## 后端业务逻辑



## 小结

- 好家伙、握手拦截、建立连接处理、Session管理，要写这么多配置，更麻烦
- 虽然麻烦，实际开发中需求复杂的话，可以在上述的写法中加入很多自定义的逻辑


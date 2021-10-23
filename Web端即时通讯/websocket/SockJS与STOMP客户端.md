来源：[SockJS简单介绍_John_62的博客-CSDN博客](https://blog.csdn.net/john_62/article/details/78208177)

# SockJS客户端

SockJS是一个浏览器JavaScript库，它提供了一个类似于网络的对象。SockJS提供了一个连贯的、跨浏览器的Javascript API，它在浏览器和web服务器之间创建了一个低延迟、全双工、跨域通信通道。

## **产生的原因**

一些浏览器中缺少对WebSocket的支持,因此，回退选项是必要的，而Spring框架提供了基于SockJS协议的透明的回退选项。

SockJS的一大好处在于提供了浏览器兼容性。优先使用原生WebSocket，如果在不支持websocket的浏览器中，会自动降为轮询的方式。
除此之外，spring也对socketJS提供了支持。

如果服务端代码中添加了withSockJS()如下，服务器也会自动降级为轮询。

```Java
registry.addEndpoint("/coordination").withSockJS();
```

SockJS的目标是让应用程序使用WebSocket API，但在运行时需要在必要时返回到非WebSocket替代，即无需更改应用程序代码。

SockJS是为在浏览器中使用而设计的。它使用各种各样的技术支持广泛的浏览器版本。对于SockJS传输类型和浏览器的完整列表，可以看到SockJS客户端页面。
传输分为3类:**WebSocket、HTTP流和HTTP长轮询**（按优秀选择的顺序分为3类）

## **使用**

SockJS 很容易通过 Java 配置启用

```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myHandler(), "/myHandler").withSockJS();
    }

    @Bean
    public WebSocketHandler myHandler() {
        return new MyHandler();
    }
}
```

- 打开一个连接，为连接创建事件监听器，断开连接，消息时间，发送消息返回到服务器，关闭连接。

```js
<script src="//cdn.jsdelivr.net/sockjs/1.0.0/sockjs.min.js"></script>
//注意：SockJS传的url是http开头的，而不是ws
var sock = new SockJS('/coordination');  
sock.onopen = function() {
    console.log('open');
};
sock.onmessage = function(e) {
    console.log('message', e.data);
};
sock.onclose = function() {
    console.log('close');
};
sock.send('test');
sock.close();
```

## 心跳消息

SockJS协议要求服务器发送心跳消息，以阻止代理结束连接。Spring SockJS配置有一个名为“心脏节拍时间”的属性，可用于定制频率。默认情况下，如果没有在该连接上发送其他消息，则会在25秒后发送心跳(stomp默认是10s)。

当在websocket/SockJS中使用STOMP时，如果客户端和服务器通过协商来交换心跳，那么SockJS的心跳就会被禁用。

Spring SockJS支持还允许配置task调度程序来调度心跳任务。

## 其他

**Servlet 3异步请求**

HTTP流和HTTP长轮询SockJS传输需要一个连接保持比平常更长时间的连接。
在Servlet容器中，这是通过Servlet 3的异步支持完成的，这允许退出Servlet的容器线程处理一个请求，并继续从另一个线程中写入响应。

如果允许跨源请求，那么SockJS协议使用CORS在XHR流和轮询传输中跨域支持。

**SockJS的CROS Headers**

如果允许跨源请求，那么SockJS协议使用CORS在XHR流和轮询传输中跨域支持。

## WebSocket、SockJs、STOMP三者关系

WebSocket 是底层协议，SockJS 是WebSocket 的备选方案，也是 底层协议，而 STOMP 是基于 WebSocket（SockJS） 的上层协议



## 相关网站

官网：[SockJS (github.com)](https://github.com/sockjs)

sockjs-client GitHub： [sockjs/sockjs-client: WebSocket emulation - Javascript client (github.com)](https://github.com/sockjs/sockjs-client)



# STOMP客户端

## 如何选择

- 作者：jmesnil，元老，github上好多STOMP库都是fork这个人的库，现已弃坑多年：[jmesnil/stomp-websocket: Stomp client for Web browsers and node.js apps (github.com)](https://github.com/jmesnil/stomp-websocket)


- 作者：JSteunou，在jmesnil弃坑后接手，虽然是Spring官方推荐的stomp客户端库，但是现在也已经弃坑，[JSteunou/webstomp-client: Stomp client over websocket for browsers (github.com)](https://github.com/JSteunou/webstomp-client)

- 新时代：**stompjs**，jmesnil也是这个项目的作者之一，地址：[stomp-js/stompjs: Javascript and Typescript Stomp client for Web browsers and node.js apps (github.com)](https://github.com/stomp-js/stompjs)，JavaScript和TypeScript 都能用，还提供了与RxJS、Angular兼容的版本，自带TypeScript 类型定义，2021年9月还在更新。

  
  - [stomp-websocket ](https://github.com/stomp-js/stomp-websocket)，也是stompjs团队的项目，forked from jmesnil的stomp-websocket，但是目前也已经弃坑，新版本就是stompjs
  
  

安装：

```
npm install xxx
```

教程:[StompJS使用文档总结：如何创建stomp客户端、如何连接服务器、心跳机制、如何发送消息、如何订阅和取消订阅、事务、如何调试 - 古兰精 - 博客园 (cnblogs.com)](https://www.cnblogs.com/goloving/p/10746378.html)

stomp自带教程：

-  [Using STOMP with SockJS - StompJS Family (stomp-js.github.io)](https://stomp-js.github.io/guide/stompjs/rx-stomp/ng2-stompjs/using-stomp-with-sockjs.html)
- [StompJs Family - StompJS Family (stomp-js.github.io)](https://stomp-js.github.io/)

## 例子

虽然stomp客户端库有很多，但是常用的api是基本一样的

使用SockJS建立websocket


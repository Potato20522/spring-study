# STOMP给特定用户发订阅消息

## @SendToUser

来源：https://www.baeldung.com/spring-websockets-sendtouser

**websocket stomp配置：**



```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic/", "/queue/");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/greeting");
    }	
}
```

**通过拦截器获取SessionID**

获取SessionID的其中一种方法是在websocket握手时从请求数据中获取，将下面的方法写到上面的配置类中

```java
@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
        
registry
  .addEndpoint("/greeting")
  .setHandshakeHandler(new DefaultHandshakeHandler() {

      public boolean beforeHandshake(
        ServerHttpRequest request, 
        ServerHttpResponse response, 
        WebSocketHandler wsHandler,
        Map attributes) throws Exception {
 
            if (request instanceof ServletServerHttpRequest) {
                ServletServerHttpRequest servletRequest
                 = (ServletServerHttpRequest) request;
                HttpSession session = servletRequest
                  .getServletRequest().getSession();
                attributes.put("sessionId", session.getId());
            }
                return true;
        }}).withSockJS();
    }
```

**Controller**

@SendToUser注解，隐含前缀：/user，将controller方法的返回值通过路径发送给制定用户，两种思路

- /user/{user}/路径，需要用户登陆，有时候，我们的应用不需要或不便引入用户登录，这时就需要sessioId

- /user/{sessionId}/路径

```java
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    private Gson gson = new Gson();
 
    @MessageMapping("/message")
    @SendToUser("/queue/reply")
    public String processMessageFromClient(@Payload String message, Principal principal) throws Exception {
	return gson.fromJson(message, Map.class).get("name").toString();
    }
	
    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
```

**客户端**

```js
function connect() {
    var socket = new WebSocket('ws://localhost:8080/greeting');
    ws = Stomp.over(socket);

    ws.connect({}, function(frame) {
        ws.subscribe("/user/queue/errors", function(message) {
            alert("Error " + message.body);
        });

        ws.subscribe("/user/queue/reply", function(message) {
            alert("Message " + message.body);
        });
    }, function(error) {
        alert("STOMP error " + error);
    });
}

function disconnect() {
    if (ws != null) {
        ws.close();
    }
    setConnected(false);
    console.log("Disconnected");
}
```


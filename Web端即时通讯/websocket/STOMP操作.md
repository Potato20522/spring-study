来源：[Spring消息之STOMP - JMCui - 博客园 (cnblogs.com)](https://www.cnblogs.com/jmcui/p/8999998.html)

# 简介

  直接使用WebSocket（或SockJS）就很类似于使用TCP套接字来编写Web应用。因为没有高层级的线路协议（wire protocol），因此就需要我们定义应用之间所发送消息的语义，还需要确保连接的两端都能遵循这些语义。

  就像HTTP在TCP套接字之上添加了请求-响应模型层一样，STOMP在WebSocket之上提供了一个基于帧的线路格式（frame-based wire format）层，用来定义消息的语义。

  与HTTP请求和响应类似，STOMP帧由命令、一个或多个头信息以及负载所组成。例如，如下就是发送数据的一个STOMP帧：

```
>>> SEND
transaction:tx-0
destination:/app/marco
content-length:20

{"message":"Marco!"}
```

# 服务端实现

## 启用STOMP功能

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig extends WebSocketMessageBrokerConfigurer {

    /**
     * 将 "/stomp" 注册为一个 STOMP 端点。这个路径与之前发送和接收消息的目的地路径有所
     * 不同。这是一个端点，客户端在订阅或发布消息到目的地路径前，要连接到该端点。
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp").withSockJS();
    }


    /**
     * 如果不重载它的话，将会自动配置一个简单的内存消息代理，用它来处理以"/topic"为前缀的消息
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //基于内存的STOMP消息代理,每个方法可以设置多个
        //可以在enableSimpleBroker里也加个"/user",这样客户端就不需要订阅/user/queue/xxx了，只需要调用/user/xxx
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setApplicationDestinationPrefixes("/app", "/foo");//客户端send前缀
        registry.setUserDestinationPrefix("/user");
    }
}
```

STOMP 的消息根据前缀的不同分为三种。

- ApplicationDestinationPrefixes  /app 开头的消息都会被路由到带有@MessageMapping 或 @SubscribeMapping 注解的方法中
- 以/topic 或 /queue 开头的消息都会发送到STOMP代理中，根据你所选择的STOMP代理不同，目的地的可选前缀也会有所限制
- 以/user开头的消息会将消息重路由到某个用户独有的目的地上。

![img](img/STOMP操作.assets/1153954-20180506222500876-639399590.png)

## 处理(接收)来自客户端的STOMP消息

服务端处理客户端发来的STOMP消息，主要用的是 @MessageMapping 注解。如下：

```java
@MessageMapping("/marco") //隐含前缀:/app
@SendTo("/topic/marco") //发向客户端的路径（要写全）,客户端需要订阅这个路径
public Shout stompHandle(Shout shout){
    LOGGER.info("接收到消息：" + shout.getMessage());
    Shout s = new Shout();
    s.setMessage("Polo!");
    //返回值通过@send发出去，不写的@send或，@send没有指定路径，那么默认路径就是@MessageMapping注解的路径再加前缀：enableSimpleBroker设定的
    return s;
}
```

- @MessageMapping 指定目的地是“/app/marco”（“/app”前缀是隐含的，因为我们将其配置为应用的目的地前缀,由setApplicationDestinationPrefixes指定）。

- 方法接收一个Shout参数（业务对象Dto），因为Spring的某一个消息转换器会将STOMP消息的负载转换为Shout对象。Spring 提供了几个消息转换器，作为其消息API的一部分：字节数组、json、text

- 尤其注意，这个处理器方法有一个返回值，这个返回值并不是返回给客户端的，而是转发给消息代理的，如果客户端想要这个返回值的话，只能从消息代理订阅。@SendTo 注解重写了消息代理的目的地，如果不指定@SendTo，帧所发往的目的地会与触发处理器方法的目的地相同，只不过会添加上“/topic”前缀。

- 如果客户端就是想要服务端直接返回消息呢？听起来不就是**HTTP**做的事情！即使这样，STOMP 仍然为这种一次性的响应提供了支持，用的是**@SubscribeMapping**注解，与HTTP不同的是，这种请求-响应模式是异步的...

  ```java
  @SubscribeMapping("/getShout")
  public Shout getShout(){
      Shout shout = new Shout();
      shout.setMessage("Hello STOMP");
      return shout;//返回值不走代理，直接到客户端订阅方法的回调里
  }
  ```

## 发送消息到客户端

### 在处理消息之后发送消息

-  正如前面看到的那样，使用 @MessageMapping 或者 @SubscribeMapping 注解可以处理客户端发送过来的消息，并选择方法是否有返回值。、
- 如果 @MessageMapping 注解的控制器方法有返回值的话，返回值会被发送到消息代理，只不过会添加上"/topic"前缀。可以使用@SendTo 重写消息目的地
-  如果 @SubscribeMapping 注解的控制器方法有返回值的话，返回值会直接发送到客户端，不经过代理。如果加上@SendTo 注解的话，则要经过消息代理

### 在应用的任意地方发送消息

spring-websocket 定义了一个 SimpMessageSendingOperations 接口（或者使用SimpMessagingTemplate ），可以实现自由的向任意目的地发送消息，并且**订阅此目的地的所有用户都能收到消息**

```java
@Autowired
  private SimpMessageSendingOperations simpMessageSendingOperations;


  /**
  * 广播消息，不指定用户，所有订阅此的用户都能收到消息
  * @param shout
  */
  @MessageMapping("/broadcastShout")
  public void broadcast(Shout shout) {
      simpMessageSendingOperations.convertAndSend("/topic/shouts", shout);
  }
```

### 为指定用户发送消息

介绍了如何广播消息，订阅目的地的所有用户都能收到消息。如果消息只想发送给特定的用户呢？spring-websocket 介绍了两种方式来实现这种功能，一种是 基于@SendToUser注解和Principal参数，一种是SimpMessageSendingOperations 接口的convertAndSendToUser方法。

#### 基于@SendToUser注解和Principal参数

```java
@MessageMapping("/shout") //用户发送的路径：/app/shout
@SendToUser("/queue/notifications") //用户订阅的路径/user/queue/notifications-'username'
public Shout userStomp(Principal principal, Shout shout) {
    String name = principal.getName();
    String message = shout.getMessage();
    LOGGER.info("认证的名字是：{}，收到的消息是：{}", name, message);
    return shout;
}
```

@SendToUser 表示要将消息发送给指定的用户，会自动在消息目的地前补上"/user"前缀(由setUserDestinationPrefix设定)。如下，**最后消息会被发布在 /user/queue/notifications-username**。但是问题来了，这个username是怎么来的呢？就是通过 **principal 参数**来获得的。那么，principal 参数又是怎么来的呢？需要在spring-websocket 的配置类中重写 configureClientInboundChannel 方法，添加上用户的认证。

```java
/**
     * 1、设置拦截器
     * 2、首次连接的时候，获取其Header信息，利用Header里面的信息进行权限认证
     * 3、通过认证的用户，使用 accessor.setUser(user); 方法，将登陆信息绑定在该 StompHeaderAccessor 上，在Controller方法上可以获取 StompHeaderAccessor 的相关信息
     * @param registration
     */
@Override
public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new ChannelInterceptorAdapter() {
        @Override
        public Message<?> preSend(Message<?> message, MessageChannel channel) {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
            //1、判断是否首次连接
            if (StompCommand.CONNECT.equals(accessor.getCommand())){
                //2、判断用户名和密码
                String username = accessor.getNativeHeader("username").get(0);
                String password = accessor.getNativeHeader("password").get(0);

                if ("admin".equals(username) && "admin".equals(password)){
                    Principal principal = new Principal() {
                        @Override
                        public String getName() {
                            return userName;
                        }
                    };
                    accessor.setUser(principal);
                    return message;
                }else {
                    return null;
                }
            }
            //不是首次连接，已经登陆成功
            return message;
        }

    });
}
```



#### convertAndSendToUser方法

除了convertAndSend()以外，SimpMessageSendingOperations 还提供了convertAndSendToUser()方法。按照名字就可以判断出来，convertAndSendToUser()方法能够让我们给特定用户发送消息。

```java
@MessageMapping("/singleShout")
public void singleUser(Shout shout, StompHeaderAccessor stompHeaderAccessor) {
    String message = shout.getMessage();
    LOGGER.info("接收到消息：" + message);
    Principal user = stompHeaderAccessor.getUser();
    simpMessageSendingOperations.convertAndSendToUser(user.getName(), "/queue/shouts", shout);
}
```

 如上，这里虽然我还是用了认证的信息得到用户名。但是，其实大可不必这样，因为 convertAndSendToUser 方法可以指定要发送给哪个用户。也就是说，**完全可以把用户名的当作一个参数传递给控制器方法，从而绕过身份认证**！convertAndSendToUser 方法最终会把**消息发送到 /user/sername/queue/shouts 目的地上**。

## 处理消息异常

在处理消息的时候，有可能会出错并抛出异常。因为STOMP消息异步的特点，发送者可能永远也不会知道出现了错误。@MessageExceptionHandler标注的方法能够处理消息方法中所抛出的异常。我们可以把错误发送给用户特定的目的地上，然后用户从该目的地上订阅消息，从而用户就能知道自己出现了什么错误啦...

```java
@MessageExceptionHandler(Exception.class)
@SendToUser("/queue/errors")
public Exception handleExceptions(Exception t){
    t.printStackTrace();
    return t;
}
```

# 客户端实现

STOMP 依赖 sockjs.js 和 stomp.min.js

```js
/*STOMP*/
var url = 'http://localhost:8080/stomp';
var sock = new SockJS(url);
var stomp = Stomp.over(sock);

var strJson = JSON.stringify({'message': 'Marco!'});

//默认的和STOMP端点连接
/*stomp.connect("guest", "guest", function (franme) {

});*/

var headers={
    username:'admin',
    password:'admin'
};

stomp.connect(headers, function (frame) {

    //发送消息
    //第二个参数是一个头信息的Map，它会包含在STOMP的帧中
    //事务支持
    var tx = stomp.begin();
    stomp.send("/app/marco", {transaction: tx.id}, strJson);
    tx.commit();


    //订阅服务端消息 subscribe(destination url, callback[, headers])
    stomp.subscribe("/topic/marco", function (message) {
        var content = message.body;
        var obj = JSON.parse(content);
        console.log("订阅的服务端消息：" + obj.message);
    }, {});


    stomp.subscribe("/app/getShout", function (message) {
        var content = message.body;
        var obj = JSON.parse(content);
        console.log("订阅的服务端直接返回的消息：" + obj.message);
    }, {});


    /*以下是针对特定用户的订阅*/
    var adminJSON = JSON.stringify({'message': 'ADMIN'});
    /*第一种*/
    stomp.send("/app/singleShout", {}, adminJSON);
    stomp.subscribe("/user/queue/shouts",function (message) {
        var content = message.body;
        var obj = JSON.parse(content);
        console.log("admin用户特定的消息1：" + obj.message);
    });
    /*第二种*/
    stomp.send("/app/shout", {}, adminJSON);
    stomp.subscribe("/user/queue/notifications",function (message) {
        var content = message.body;
        var obj = JSON.parse(content);
        console.log("admin用户特定的消息2：" + obj.message);
    });

    /*订阅异常消息*/
    stomp.subscribe("/user/queue/errors", function (message) {
        console.log(message.body);
    });

    //若使用STOMP 1.1 版本，默认开启了心跳检测机制（默认值都是10000ms）
    stomp.heartbeat.outgoing = 20000;

    stomp.heartbeat.incoming = 0; //客户端不从服务端接收心跳包
});
```

# token鉴权相关

权限相关一般是增加拦截器，网上查到的资料一般有两种方式：

- 实现`HandshakeInterceptor`接口在`beforeHandshake`方法中来处理，这种方式缺点是无法获取`header`中的值，只能获取`url`中的参数，如果`token`用`jwt`等很长的，用这种方式实现并不友好。
- **实现`ChannelInterceptor`接口在`preSend`方法中来处理，这种方式可以获取`header`中的值**，而且还可以设置用户信息等，详细见下方拦截器代码

**vue端相关注意点**

- `vue`端用`websocket`的好处是单页应用，不会频繁的断开和重连，所以相关代码放到`App.vue`中
- 由于要鉴权，所以需要登录后再连接，这里用的方法是`watch`监听`token`，如果`token`从无到有，说明刚登录，触发`websocket`连接。
- 前端引入包`npm instll sockjs-client` 和 `npm install stompjs`，具体代码见下方。

后台配置

```java
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
  @Autowired
  private AuthChannelInterceptor authChannelInterceptor;

  @Bean
  public WebSocketInterceptor getWebSocketInterceptor() {
      return new WebSocketInterceptor();
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
      registry.addEndpoint("/ws")//请求地址:http://ip:port/ws
              .addInterceptors(getWebSocketInterceptor())//拦截器方式1,暂不用
              .setAllowedOrigins("*")//跨域
              .withSockJS();//开启socketJs
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
      registry.enableSimpleBroker("/queue", "/topic");
      registry.setApplicationDestinationPrefixes("/app");
      registry.setUserDestinationPrefix("/user");
  }

  /**
   * 拦截器方式2
   *
   * @param registration
   */
  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
      registration.interceptors(authChannelInterceptor);
  }
}
```

拦截器

```java
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class AuthChannelInterceptor implements ChannelInterceptor {
  /**
   * 连接前监听
   *
   * @param message
   * @param channel
   * @return
   */
  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
      StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
      //1、判断是否首次连接
      if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
          //2、判断token
          List<String> nativeHeader = accessor.getNativeHeader("Authorization");
          if (nativeHeader != null && !nativeHeader.isEmpty()) {
              String token = nativeHeader.get(0);
              if (StringUtils.isNotBlank(token)) {
                  //todo,通过token获取用户信息，下方用loginUser来代替
                  if (loginUser != null) {
                      //如果存在用户信息，将用户名赋值，后期发送时，可以指定用户名即可发送到对应用户
                      Principal principal = new Principal() {
                          @Override
                          public String getName() {
                              return loginUser.getUsername();
                          }
                      };
                      accessor.setUser(principal);
                      return message;
                  }
              }
          }
          return null;
      }
      //不是首次连接，已经登陆成功
      return message;
  }
}
```

前端代码，放在App.vue中

```js
import Stomp from 'stompjs'
import SockJS from 'sockjs-client'
import {mapGetters} from "vuex";

export default {
  name: 'App',
  data() {
    return {
      stompClient: null,
    }
  },
  computed: {
    ...mapGetters(["token"])
  },
  created() {
    //只有登录后才连接
    if (this.token) {
      this.initWebsocket();
    }
  },
  destroyed() {
    this.closeWebsocket()
  },
  watch: {
    token(val, oldVal) {
      //如果一开始没有，现在有了，说明刚登录，连接websocket
      if (!oldVal && val) {
        this.initWebsocket();
      }
      //如果原先由，现在没有了，说明退出登录，断开websocket
      if (oldVal && !val) {
        this.closeWebsocket();
      }
    }
  },
  methods: {
    initWebsocket() {
      let socket = new SockJS('http://localhost:8060/ws');
      this.stompClient = Stomp.over(socket);
      this.stompClient.connect(
        {"Authorization": this.token},//传递token
        (frame) => {
          //测试topic
          this.stompClient.subscribe("/topic/subscribe", (res) => {
            console.log("订阅消息1:");
            console.log(res);
          });
          //测试 @SubscribeMapping
          this.stompClient.subscribe("/app/subscribe", (res) => {
            console.log("订阅消息2:");
            console.log(res);
          });
          //测试单对单
          this.stompClient.subscribe("/user/queue/test", (res) => {
            console.log("订阅消息3:");
            console.log(res.body);
          });
          //测试发送
          this.stompClient.send("/app/test", {}, JSON.stringify({"user": "user"}))
        },
        (err) => {
          console.log("错误：");
          console.log(err);
          //10s后重新连接一次
          setTimeout(() => {
            this.initWebsocket();
          }, 10000)
        }
      );
      this.stompClient.heartbeat.outgoing = 20000; //若使用STOMP 1.1 版本，默认开启了心跳检测机制（默认值都是10000ms）
      this.stompClient.heartbeat.incoming = 0; //客户端不从服务端接收心跳包
    },
    closeWebsocket() {
      if (this.stompClient !== null) {
        this.stompClient.disconnect(() => {
          console.log("关闭连接")
        })
      }
    }
  }
}
```

**新版本是不推荐用SockJS的，理由是现在大多数浏览器除了旧的IE，其它的都支持**，所以如果不用的话，前端直接用`brokerURL`而不需要用`webSocketFactory`来配置了，后端配置项需要修改，参考[这个回答](https://stackoverflow.com/a/68076180/7197770)：

```java
@Override
public void registerStompEndpoints(StompEndpointRegistry registry) {
    //允许原生的websocket
    registry.addEndpoint("/ws")//请求地址:ws://ip:port/ws
        .setAllowedOrigins("*");//跨域
    //允许sockJS
    registry.addEndpoint("/ws")//请求地址:http://ip:port/ws
        .setAllowedOrigins("*")//跨域
        .withSockJS();//开启sockJs
}
```


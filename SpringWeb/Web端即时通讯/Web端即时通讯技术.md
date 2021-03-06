# Web端即时通讯技术

来源：

- [浅析Web实时通信的方法总结：短轮询/长轮询/SSE/Websocket的介绍及性能和兼容性对比、SSE的工作原理及如何实现 - 古兰精 - 博客园 (cnblogs.com)](https://www.cnblogs.com/goloving/p/9170657.html)

- [web端四种即时通讯_伍柳森的博客-CSDN博客](https://blog.csdn.net/qq_41248532/article/details/116450754)
- [Js即时通讯技术 - SSE（SpringBoot实现）_阿瑟与非-CSDN博客_js 即时通讯](https://blog.csdn.net/cs373616511/article/details/113825561)

　    即时通讯技术简单的说就是实现这样一种功能：服务器端可以即时地将数据的更新或变化反应到客户端，例如消息即时推送等功能都是通过这种技术实现的。

　　但是在Web中，由于浏览器的限制，实现即时通讯需要借助一些方法。这种限制出现的主要原因是，一般的Web通信都是浏览器先发送请求到服务器，服务器再进行响应完成数据的现实更新。

实现即时通讯主要有四种方式，它们分别是：短轮询、长轮询(comet)、长连接(SSE)、WebSocket

它们大体可以分为两类，一种是在HTTP基础上实现的，包括短轮询、comet和SSE；另一种不是在HTTP基础上实现是，即WebSocket。下面分别介绍一下这四种轮询方式，以及它们各自的优缺点。

## 短轮询

短轮询的基本思路就是浏览器**每隔一段时间向浏览器发送http请求**，服务器端在收到请求后，不论是否有数据更新，都直接进行响应。

　　这种方式实现的即时通信，本质上还是浏览器发送请求，服务器接受请求的一个过程，通过让客户端不断的进行请求，使得客户端能够模拟实时地收到服务器端的数据的变化。

　　这种方式的优点是比较简单，易于理解，实现起来也没有什么技术难点。缺点是显而易见的，这种方式由于需要不断的建立http连接，严重浪费了服务器端和客户端的资源。尤其是在客户端，距离来说，如果有数量级想对比较大的人同时位于基于短轮询的应用中，那么每一个用户的客户端都会疯狂的向服务器端发送http请求，而且不会间断。人数越多，服务器端压力越大，这是很不合理的。

　　因此短轮询不适用于那些同时在线用户数量比较大，并且很注重性能的Web应用。

## comet -长轮询

comet 指的是，当服务器收到客户端发来的请求后，**不会直接进行响应，而是先将这个请求挂起**，然后判断服务器端数据是否有更新。如果有更新，则进行响应，如果一直没有数据，则到达一定的时间限制（服务器端设置）后关闭连接。

　　长轮询和短轮询比起来，明显减少了很多不必要的http请求次数，相比之下节约了资源。长轮询的缺点在于，**连接挂起也会导致资源的浪费**。

- Springboot中通过异步来实现：@EnableAsync , @Sync
- 前端：AJAX 长轮询

## SSE-长连接

SSE是HTML5新增的功能，全称为Server-SentEvents。它可以允许服务推送数据到客户端。SSE在本质上就与之前的长轮询、短轮询不同，虽然都是基于http协议的，但是轮询需要客户端先发送请求。

　　而SSE最大的特点就是不需要客户端发送请求，可以实现只要服务器端数据有更新，就可以马上发送到客户端。

　　SSE的优势很明显，它不需要建立或保持大量的客户端发往服务器端的请求，节约了很多资源，提升应用性能。SSE的实现非常简单，并且不需要依赖其他插件。

- 在SpringMVC中有对象SseEmitter，用来收、发消息
- js中有个对象是EventSource，可以用来收、发消息

## WebSocket

WebSocket是HTML5定义的一个新协议，与传统的http协议不同，该协议可以实现服务器与客户端之间全双工通信。

　　简单来说，首先需要在客户端和服务器端建立起一个连接，这部分需要http。连接一旦建立，**客户端和服务器端就处于平等的地位，可以相互发送数据，不存在请求和响应的区别**。

　　WebSocket的优点是实现了**双向通信**，**缺点是服务器端的逻辑非常复杂**。现在针对不同的后台语言有不同的插件可以使用。

## 总结

从**兼容性角度考虑，短轮询>长轮询>长连接SSE>WebSocket**

**从性能方面考虑，WebSocket>长连接SSE>长轮询>短轮询**


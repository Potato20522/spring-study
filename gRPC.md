# 学习资源

[gRPC](https://www.grpc.io/)

[Springboot整合gRPC_额滴神的博客-CSDN博客](https://blog.csdn.net/weixin_40395050/article/details/96971708)

[Introduction to gRPC | Baeldung](https://www.baeldung.com/grpc-introduction)

# 概述



rRPC是高性能开源的RPC框架，由谷歌开发。

- **基于HTTP/2**
  HTTP/2 提供了连接多路复用、双向流、服务器推送、请求优先级、首部压缩等机制。可以节省带宽、降低TCP链接次数、节省CPU，帮助移动设备延长电池寿命等。gRPC 的协议设计上使用了HTTP2 现有的语义，请求和响应的数据使用HTTP Body 发送，其他的控制信息则用Header 表示。
- **IDL使用ProtoBuf**
  gRPC使用ProtoBuf来定义服务，ProtoBuf是由Google开发的一种数据序列化协议（类似于XML、JSON、hessian）。ProtoBuf能够将数据进行序列化，并广泛应用在数据存储、通信协议等方面。压缩和传输效率高，语法简单，表达力强。
- **多语言支持**（C, C++, Python, PHP, Nodejs, C#, Objective-C、Golang、Java）
  gRPC支持多种语言，并能够基于语言自动生成客户端和服务端功能库。目前已提供了C版本grpc、Java版本grpc-java 和 Go版本grpc-go，其它语言的版本正在积极开发中，其中，grpc支持C、C++、Node.js、Python、Ruby、Objective-C、PHP和C#等语言，grpc-java已经支持Android开发。

**优点**

protobuf二进制消息，性能好/效率高（空间和时间效率都很不错）
proto文件生成目标代码，简单易用
序列化反序列化直接对应程序中的数据类，不需要解析后在进行映射(XML,JSON都是这种方式)
支持向前兼容（新加字段采用默认值）和向后兼容（忽略新加字段），简化升级
支持多种语言（可以把proto文件看做IDL文件）
Netty等一些框架集成

**缺点**

1）GRPC尚未提供连接池，需要自行实现
2）尚未提供“服务发现”、“负载均衡”机制
3）因为基于HTTP2，绝大部多数HTTP Server、Nginx都尚不支持，即Nginx不能将GRPC请求作为HTTP请求来负载均衡，而是作为普通的TCP请求。（nginx1.9版本已支持）
4） Protobuf二进制可读性差（貌似提供了Text_Fromat功能）
默认不具备动态特性（可以通过动态定义生成消息类型或者动态编译支持）

# 基本使用


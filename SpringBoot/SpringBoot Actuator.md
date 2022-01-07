# spring boot actuator监控

[spring boot actuator监控详细介绍一(超级详细)_X兄的博客-CSDN博客](https://blog.csdn.net/weixin_43353498/article/details/89226101)

## 介绍

做什么的

- 可以在生产环境进行监控和管理SpringBoot
- 可以选择使用HTTP端点或JMX来管理和监视应用程序
- 审核，运行状况和指标收集



- 显示应用的Health健康信息
- 显示Info应用信息
- 显示HTTP Request跟踪信息
- 显示当前应用程序的“Metrics”信息
- 显示所有的@RequestMapping的路径信息
- 显示应用程序的各种配置信息
- 显示你的程序请求的次数时间等各种信息

依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

访问：http://localhost:8080/actuator 来查看有哪些监控点

## 所有的监控端点endpoints


auditevents	公开当前应用程序的审核事件信息。

beans	显示应用程序中所有Spring bean的完整列表。

caches	暴露可用的缓存。

conditions	显示在配置和自动配置类上评估的条件以及它们匹配或不匹配的原因。

configprops	显示所有的整理列表@ConfigurationProperties,查看配置属性，包括默认配置

env	露出Spring的属性的各种环境变量,后面可跟/{name}查看具体的值

flyway	显示已应用的任何Flyway数据库迁移。

health	显示应用健康信息,在spring boot2.0以后需要在配置里show-details打开所有健康信息

httptrace	显示HTTP跟踪信息（默认情况下，最后100个HTTP请求 - 响应交换）,2.0以后需要手动打开

info	显示任意应用信息,是在配置文件里自己定义的

integrationgraph	显示Spring Integration图。

loggers	显示和修改应用程序中记录器的配置。

liquibase	显示已应用的任何Liquibase数据库迁移。

metrics	显示当前应用程序的“指标”信息,比如内存用量和HTTP请求计数,后可跟/{name}查看具体值

mappings	显示所有@RequestMapping路径的整理列表。

scheduledtasks	显示应用程序中的计划任务。

sessions	允许从Spring Session支持的会话存储中检索和删除用户会话。使用Spring Session对响应式Web应用程序的支持时不可用

shutdown	允许应用程序正常关闭。

threaddump	执行线程转储。




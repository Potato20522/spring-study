## 概述

官方出的升级文档

https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Config-Data-Migration-Guide

Springboot2.4的配置新特性主要是新增：

```
spring.config.import
```

如果你的应用工程只有一个简单的 application.properties 或 application.yml 文件，则可以进行无缝升级到 Spring Boot 2.4.0。

但是，如果你的配置较为复杂，比如说有指定 profile 的参数，或者有基于 profile 激活的参数，要使用新功能就需要进行一些更改。

## 保留旧的配置风格（不推荐）

开启use-legacy-processing后，配置文件还是可以像以前一样的写，不用新的风格，但是不利于今后长远的升级

```properties
spring.config.use-legacy-processing=true
```

## 可引入jar包外配置文件





## 迁移

### profiles变更

旧：

```yaml
spring:
  profiles: "prod"
secret: "production-password"
```


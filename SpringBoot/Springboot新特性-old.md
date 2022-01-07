# 说明

新特性发布wiki:https://github.com/spring-projects/spring-boot/wiki

Spring Boot大版本正式发布后，会持续支持21个月，当前阶段主要版本的生命周期：

| Version | Released      | OSS Support Until | Expected End of Life |
| ------- | ------------- | ----------------- | -------------------- |
| 2.5.x   | May 2021      | May 2022          | February 2023        |
| 2.4.x   | November 2020 | November 2021     | August 2022          |
| 2.3.x   | May 2020      | May 2021          | February 2022        |

已经停止更新的版本：

| Version | Released     | End of Life   | Notes                   |
| ------- | ------------ | ------------- | ----------------------- |
| 2.2.x   | October 2019 | July 2021     |                         |
| 2.1.x   | October 2018 | November 2020 |                         |
| 2.0.x   | March 2018   | April 2019    |                         |
| 1.5.x   | January 2017 | August 2019   | Last in the `1.x` line. |

# Springboot2.3

## 支持docker分层Jar

https://my.oschina.net/giegie/blog/4289643



# Springboot2.4

https://nowjava.com/news/44061

## 配置改进

**1、改进配置文件的处理方式**

Spring Boot 2.4 改进了处理 application.properties 和 application.yml 配置文件的方式。更新后的逻辑旨在简化和合理化外部配置的加载方式，但有些参数的组合形式却得到了限制，升级请谨慎。

如果你的应用工程只有一个简单的 application.properties 或 application.yml 文件，则可以进行无缝升级到 Spring Boot 2.4.0。

但是，如果你的配置较为复杂，比如说有指定 profile 的参数，或者有基于 profile 激活的参数，要使用新功能就需要进行一些更改。

更多细节可参考：

> [https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-Config-Data-Migration-Guide](https://link.nowjava.com/?target=https%3A//github.com/spring-projects/spring-boot/wiki/Spring-Boot-Config-Data-Migration-Guide)

如果你想继续使用 Spring Boot 2.3 的配置逻辑，也可以通过在 application.properties 或者 application.yml 配置文件中添加以下参数：

> spring.config.use-legacy-processing = true

**2、导入配置参数改进**

通过配置参数 spring.config.location 和 spring.config.import 来指定或者导入配置文件时，如果配置文件不存在，现在不是只能默默地失败了，可以通过添加 `optional:` 前缀来标识它是可选的。

比如我们从 /etc/config/application.properties 指定配置文件，如果这个文件不存在，系统就会跳过它。

> spring.config.location=optional:/etc/config/application.properties

如果你想将所有指定的配置文件都默认为可选的，可以通过 SpringApplication.setDefaultProperties(…) 来设置

spring.config.on-location-not-found=ignore 这个参数，或者将它设置在系统环境变量中。





。。。。。。

# Springboot2.5
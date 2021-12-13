# logback的介绍

Logback是由log4j创始人设计的另一个开源日志组件,官方网站： http://logback.qos.ch。它当前分为下面三个模块：

- logback-core：其它两个模块的基础模块
- logback-classic：它是log4j的一个改良版本，同时它**完整实现了slf4j API**使你可以很方便地更换成其它日志系统如log4j或JDK14 Logging
- logback-access：访问模块与Servlet容器集成提供通过Http来访问日志的功能
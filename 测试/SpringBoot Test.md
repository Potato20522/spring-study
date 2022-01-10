# 介绍

[测试 (spring.io)](https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#testing-introduction)

Spring提倡测试驱动开发（TDD）

单元测试和集成测试

# 单元测试

## mock对象

Spring中包含一些可以mock的包：Environment、JNDI、Servlet API、Spring Web Reactive

**Environment**： org.springframework.mock.env

可以模拟Environment和PropertySource，提供了MockEnvironment 和 MockPropertySource这两个工具类

**JNDI**：org.springframework.mock.jndi

可以mock 数据源

**Servlet API**  org.springframework.mock.web

mockMvc，可以mock controller

**Spring Web Reactive**   `org.springframework.mock.http.server.reactive`

mock Spring Web Reactive 

## 单元测试支持类

### 通用测试工具

包名 org.springframework.test.util 

**1、ReflectionTestUtils**

[单元测试中使用Spring的ReflectionTestUtils更方便 - 南瓜慢说 - 博客园 (cnblogs.com)](https://www.cnblogs.com/larrydpk/p/12853451.html)

基于反射的测试

`ReflectionUtils`是`Spring`中一个常用的类，属于`spring-core`包；`ReflectionTestUtils`则属于`spring-test`包。两者功能有重叠的地方，而`ReflectionUtils`会更强大。在单元测试时使用`ReflectionTestUtils`，能增加我们的便利性。

假设我们需要创建一个类，但它的某些成员变量是私有的，并且没有提供公共的`setter`方法，而我们无法按照它的正常初始化流程来使它的这些成员变量是我们想要的值。这时就需要想办法修改一个类的私有变量，而`反射`可以帮助到我们。`Spring`也提供了`反射`相关的工具类：`ReflectionUtils`和`ReflectionTestUtils`

**2、AopTestUtils**

[ 通过AopTestUtils对切面对象进行mock_weixin_30735745的博客-CSDN博客](https://blog.csdn.net/weixin_30735745/article/details/98140037)

​	当对一个切面类进行测试时，由于Spring对切面对象生成了proxy对象，此时对切面对象使用ReflectionTestUtils赋值，操作的是proxy对象，而不是真实对象，会使得赋值出问题。可以通过引入AopTestUtils解决赋值问题。

​	通过AopTestUtils可以通过切面proxy对象，获取到切面的真实对象。通过使用ReflectionTestUtils对真实的切面对象修改依赖，到达mock的目的。

### SpringMVC测试工具

包：org.springframework.test.web

ModelAndViewAssert

[Java ModelAndViewAssert类代码示例 - 纯净天空 (vimsky.com)](https://vimsky.com/examples/detail/java-class-org.springframework.test.web.ModelAndViewAssert.html)

# 集成测试

集成测试，无需部署项目到服务器上，直接测，可以测Spring容器上下文、测ORM（SQL语句、查询，JPA实体映射）

## JDBC测试支持

在org.springframework.test.jdbc 包下，有JdbcTestUtils这个工具类，方法：

- `countRowsInTable(..)`：计算给定表中的行数。
- `countRowsInTableWhere(..)`：使用所提供的条款计算给定表中的行数。`WHERE`
- `deleteFromTables(..)`：从指定的表中删除所有行。
- `deleteFromTableWhere(..)`：使用所提供的条款从给定表中删除行。`WHERE`
- `dropTables(..)`：删除指定的表。

## 注解

Spring提供了一些可以在测试中使用的注解：

### @BootstrapWith

标在类上，可以自定义类的引导

### @ContextConfiguration

加载上下文配置applicationcontext.xml 或加载配置类，一般用不到

[ @ContextConfiguration注解说明_碧海凌云的博客-CSDN博客](https://blog.csdn.net/u012260238/article/details/87462366)

[测试类添加@ContextConfiguration注解_工大枸杞微铺的博客-CSDN博客](https://blog.csdn.net/weixin_45466462/article/details/114653521)



### @WebAppConfiguration

[Spring中Junit测试-@WebAppConfiguration与WebApplicationContext_qq_三哥啊的博客-CSDN博客](https://blog.csdn.net/qq_27579471/article/details/113725329)

为测试加载WebApplicationContext

## Spring TestContext 框架



## WebTestClient



## MockMvc



## 测试客户端应用
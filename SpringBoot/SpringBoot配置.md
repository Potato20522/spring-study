## 全部配置

https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#application-properties



## 不占用端口启动

[springboot不占用端口启动 - 陈灬大灬海 - 博客园 (cnblogs.com)](https://www.cnblogs.com/chywx/p/11234527.html)

### 非Web工程

在服务架构中，有些springboot工程只是简单的作为服务，并不提供web服务

这个时候不需要依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

但是启动springboot的话，启动之后就会自动关闭，可以通过如下方式解决

实现**CommandLineRunner**，重写**run**方法即可，这样启动后就不会关闭

```java
@SpringBootApplication
@EnableDubbo
public class SeaProviderLogApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SeaProviderLogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("SeaProviderLogApplication正在启动。。。");
        while(true) {
            Thread.sleep(600000000);
            System.out.println("sleep....");
        }
    }
}
```

有人可能会说，引入**spring-boot-starter-web**主要是为了方便测试，其实完全可以使用单元测试进行操作

使用**@SpringBootTest**和**@RunWith(SpringRunner.class)**注解即可进行单元测试*代码如下*

```java
@SpringBootTest
@RunWith(SpringRunner.class)
public class IndexControllerTest {

    @Reference(version = "1.0.1")
    private ErrorLogService errorLogService;

    @Test
    public void bbb() {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setName("error");
        System.out.println(errorLogService.sendMsg(errorLog));
    }
}
```

### web工程

 但是有时候由于**maven**聚合工程，会依赖**common**或者**parent**，会自然的引入了

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

 这个时候启动的话，默认端口是8080，当然是可以在application.properties中配置

server.port=8081 来进行修改，但是比较麻烦，因为本就不暴露http请求，没必要添加spring-boot-starter-web依赖，服务多的话也端口设置也让人头疼，会产生端口占用问题

由于不提供web服务，属实没必要暴露端口，可以通过如下两种方式进行启动不设置端口号

**第一种：**

修改**application**配置文件

```yaml
spring:
  main:
    allow-bean-definition-overriding: true
    web-application-type: none
```

**第二种：**

修改**启动入口**

```java
public static void main(String[] args) {
    new SpringApplicationBuilder(Application .class)
        .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
        .run(args);
}
```


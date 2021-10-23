# Spring Bean作用域

[Spring系列四：Bean Scopes作用域 - niocoder - 博客园 (cnblogs.com)](https://www.cnblogs.com/merryyou/p/11708013.html)

在`Spring`框架中，我们可以在六个内置的`spring bean`作用域中创建`bean`，还可以定义`bean`范围。在这六个范围中，只有在使用支持`Web`的`applicationContext`时，其中四个可用。`singleton`和`prototype`作用域可用于任何类型的`ioc`容器。

## **Spring Bean作用域类型**

在`Spring`中，可以使用`spring`中的 `@Scope`注解定义`bean`的作用域。下面我们已经列出这六个在`Spring`应用程序上下文中使用的内置`bean`作用域。这些相同的作用域也适用于`spring boot` `bean`作用域。

| SCOPE         | 描述                                                         |
| ------------- | ------------------------------------------------------------ |
| `singleton`   | `spring IoC`容器存在一个`bean`对象实例。                     |
| `prototype`   | 与单例相反，每次请求`bean`时，它都会创建一个新实例。         |
| `request`     | 在`HTTP`请求(`Request`) 的完整生命周期中，将创建并使用单个实例。 只适用于`web`环境中`Spring` `ApplicationContext`中有效。 |
| `session`     | 在`HTTP`会话(`Session`) 的完整生命周期中，将创建并使用单个实例。 只适用于`web`环境中`Spring` `ApplicationContext`中有效。 |
| `application` | 将在`ServletContext`的完整生命周期中创建并使用单个实例。只适用于`web`环境中`Spring` `ApplicationContext`中有效。 |
| `websocket`   | 在WebSocket的完整生命周期中，将创建并使用单个实例。 只适用于`web`环境中`Spring` `ApplicationContext`中有效。 |

### **单例作用域**

`singleton`是`spring`容器中bean的默认作用域。它告诉容器仅创建和管理一个`bean`类实例。该单个实例存储在此类单例`bean`的缓存中，并且对该命名`bean`的所有后续请求和引用都返回该缓存的实例。

注意Controller、Service都是单例的

```java
@Component
@Scope("singleton")  //@Scope可以省略，默认即是singleton
public class BeanClass {

}
```

### **原型作用域**

每次应用程序对`Bean`进行请求时，原型作用域都会创建一个新的`Bean`实例。

您应该知道，销毁`bean`生命周期方法不调用原型作用域`bean`，只调用初始化回调方法。因此，作为开发人员，您要负责清理原型作用域的`bean`实例以及其中包含的所有资源。

```java
@Component
@Scope("prototype")
public class BeanClass {
}
```

选择单例还是原型

- 通常，应该为所有**有状态bean使用原型范围**，为**无状态bean使用单例范围**

- 要在请求、会话、应用程序和`websocket`范围内使用`bean`，您需要注册`RequestContextListener`或`RequestContextFilter`.

### request作用域

在请求范围中，**容器为每个`HTTP`请求创建一个新实例**。因此，如果服务器当前处理50个请求，那么容器最多可以有50个`bean`类的单独实例。对一个实例的任何状态更改对其他实例都是不可见的。一旦请求完成，这些实例就会被销毁。

```java
@Component
@Scope("request")
public class BeanClass {
}
//or
@Component
@RequestScope
public class BeanClass {
}
```

### session作用域

在会话范围中，容器为每个`HTTP`会话创建一个新实例。因此，如果服务器有20个活动会话，那么容器最多可以有20个`bean`类的单独实例。在单个会话生命周期内的所有`HTTP`请求都可以访问该会话范围内相同的单个`bean`实例。

在会话范围内，对一个实例的任何状态更改对其他实例都是不可见的。一旦会话在服务器上被销毁/结束，这些实例就会被销毁。

```java
@Component
@Scope("session")
public class BeanClass {
}
//or
@Component
@SessionScope
public class BeanClass {
}
```

### application作用域

在应用程序范围内，容器为**每个`web`应用程序运行时创建一个实例。它几乎类似于单例范围**，只有两个不同之处。即：

1. **应用程序作用域`bean`是每个`ServletContext`的单例对象，而单例作用域`bean`是每个`ApplicationContext`的单例对象。请注意，单个应用程序可能有多个应用程序上下文**。
2. 应用程序作用域`bean`作为`ServletContext`属性可见。

```java
@Component
@Scope("application")
public class BeanClass {
}

//or

@Component
@ApplicationScope
public class BeanClass {
}
```

### websocket作用域

`WebSocket`协议支持客户端和远程主机之间的双向通信，远程主机选择与客户端通信。`WebSocket`协议为两个方向的通信提供了一个单独的`TCP`连接。这对于具有同步编辑和多用户游戏的多用户应用程序特别有用。

在这种类型的`Web`应用程序中，`HTTP`仅用于初始握手。如果服务器同意，服务器可以以`HTTP`状态101（交换协议）进行响应。如果握手成功，则`TCP`套接字保持打开状态，客户端和服务器都可以使用该套接字向彼此发送消息。

```java
@Component
@Scope("websocket")
public class BeanClass {
}
```

请注意，**`websocket`范围内的`bean`通常是单例的，并且比任何单独的`WebSocket`会话寿命更长**。

### 自定义线程作用域

`Spring`还使用类`SimpleThreadScope`提供了非默认线程作用域。若要使用此作用域，必须使用`CustomScopeConfigurer`类将其注册到容器。

```xml
<bean class="org.springframework.beans.factory.config.CustomScopeConfigurer">
    <property name="scopes">
        <map>
            <entry key="thread">
                <bean class="org.springframework.context.support.SimpleThreadScope"/>
            </entry>
        </map>
    </property>
</bean>
```

对`bean`的每个请求都将在同一线程中返回相同的实例。

线程`bean`范围的`Java`配置示例:

```java
@Component
@Scope("thread")
public class BeanClass {
}
```

### 自定义作用域

1. 先实现Scope接口创建自定义作用域范围类
2. 使用CustomScopeConfigurer注册自定义的作用域范围

后面写了一个例子实践一下，自定义了一种同一分钟的作用域范围，即同一分钟获取的是相同实例。

```java
/**
 * 首先自定义作用域范围类TimeScope:
 * Scope接口提供了五个方法，只有get()和remove()是必须实现，get()中写获取逻辑，
 * 如果已有存储中没有该名称的bean，则通过objectFactory.getObject()创建实例。
 */
@Slf4j
public class TimeScope implements Scope {

    private static Map<String, Map<Integer, Object>> scopeBeanMap = new HashMap<>();

    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Integer hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        // 当前是一天内的第多少分钟
        Integer minute = hour * 60 + Calendar.getInstance().get(Calendar.MINUTE);
        log.info("当前是第 {} 分钟", minute);
        Map<Integer, Object> objectMap = scopeBeanMap.get(name);
        Object object = null;
        if (Objects.isNull(objectMap)) {
            objectMap = new HashMap<>();
            object = objectFactory.getObject();
            objectMap.put(minute, object);
            scopeBeanMap.put(name, objectMap);
        } else {
            object = objectMap.get(minute);
            if (Objects.isNull(object)) {
                object = objectFactory.getObject();
                objectMap.put(minute, object);
                scopeBeanMap.put(name, objectMap);
            }
        }
        return object;
    }

    @Override
    public Object remove(String name) {
        return scopeBeanMap.remove(name);
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
    }
    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }
    @Override
    public String getConversationId() {
        return null;
    }
}
/**
 * 然后注册自定义的作用域范围：
 */
@Configuration
@Slf4j
public class BeanScopeConfig {
    @Bean
    public CustomScopeConfigurer customScopeConfigurer() {
        CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        Map<String, Object> map = new HashMap<>();
        map.put("timeScope", new TimeScope());
        customScopeConfigurer.setScopes(map);
        return customScopeConfigurer;
    }
    
    @Bean
    @Scope(value = "timeScope", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TimeScopeBean timeScopeBean() {
        TimeScopeBean timeScopeBean = new TimeScopeBean();
        timeScopeBean.setCurrentTime(System.currentTimeMillis());
        log.info("time scope bean");
        return timeScopeBean;
    }
}
然后注入调用timeScopeBean，同一分钟内重复调用，使用相同实例，不同分钟将创建新实例
```

## 作用域代理

[SpringBoot：@Scope注解学习 - 寒烟濡雨 - 博客园 (cnblogs.com)](https://www.cnblogs.com/hyry/p/11987067.html)

先通过注解的javadoc，可以了解到，@Scope在和@Component注解一起修饰在类上，作为类级别注解时，@Scope表示该类实例的范围，在和@Bean一起修饰在方法上，作为方法级别注解时，@Scope表示该方法返回的实例的范围。
对于@Scope注解，我们常用的属性一般就是：value和proxyMode，value就是指明使用哪种作用域范围，proxyMode指明使用哪种作用域代理。
@Scope定义提供了的作用域范围一般有：singleton单例、prototype原型、requestweb请求、sessionweb会话，同时我们也可以自定义作用域。

- singleton单例范围，这个是比较常见的，Spring中bean的实例默认都是单例的，单例的bean在Spring容器初始化时就被直接创建，不需要通过proxyMode指定作用域代理类型。
- prototype原型范围，这个使用较少，这种作用域的bean，每次注入调用，Spring都会创建返回不同的实例，但是，需要注意的是，如果未指明代理类型，即不使用代理的情况下，将会在容器启动时创建bean，那么每次并不会返回不同的实例，只有在指明作用域代理类型例如TARGET_CLASS后，才会在注入调用每次创建不同的实例。
- requestweb请求范围，（最近遇到的问题就是和request作用域的bean有关，才发现之前的理解有偏差），当使用该作用域范围时（包括下面的session作用域），必须指定proxyMode作用域代理类型，否则将会报错，对于request作用域的bean，（之前一直理解的是每次有http请求时都会创建），但实际上并不是这样，而是Spring容器将会创建一个代理用作依赖注入，只有在请求时并且请求的处理中需要调用到它，才会实例化该目标bean。
- sessionweb会话范围，这个和request类似，同样必须指定proxyMode，而且也是Spring容器创建一个代理用作依赖注入，当有会话创建时，并且在会话中请求的处理中需要调用它，才会实例话该目标bean，由于是会话范围，生命依赖于session。

如果指定为proxyMode = ScopedProxyMode.TARGET_CLASS，那么将使用cglib代理创建代理实例；如果指定为proxyMode = ScopedProxyMode.INTERFACE，那么将使用jdk代理创建代理实例；**如果不指定，则直接在Spring容器启动时创建该实例**。而且使用代理创建代理实例时，只有在注入调用时，才会真正创建类对象。


# spring注解@lazy，bean懒加载

来源：https://blog.csdn.net/qq_36722039/article/details/81588098

该注解是在单实例bean是使用，当使用@Scope注解的singleton属性时，bean的实例会在IOC容器创建的时候被加载，但是如果在创建bean的时候加上@lazy注解，则bean的实例会在第一次使用的时候被创建，而不是依赖注入的时候创建。

```java
@Lazy
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)//singleton
@Bean(name = "person")
public Person person(){
    Person person = new Person();
    person.setName("lqf");
    person.setEmail("lqf@163.com");
    return person;
}
```

推荐继续阅读：[SpringBoot（6）— Bean懒加载@Lazy和循环依赖处理 - 沧海一粟hr - 博客园 (cnblogs.com)](https://www.cnblogs.com/javahr/p/13405442.html)

# @DependsOn

@DependsOn注解可以定义在类和方法上，意思是我这个组件要**依赖于另一个组件**，也就是说被依赖的组件会比该组件先注册到IOC容器中，用于**强制初始化其他Bean**。可以指定一个字符串数组作为参数，每个数组元素对应于一个强制初始化的Bean。

```java
@Component
@DependsOn({"filereader", "fileWriter"})
public class FileProcessor {}
```



```java
@Configuration
public class Config {
 
    @Bean
    @DependsOn({"fileReader","fileWriter"})
    public FileProcessor fileProcessor(){
        return new FileProcessor();
    }
    
    @Bean("fileReader")
    public FileReader fileReader() {
        return new FileReader();
    }
    
    @Bean("fileWriter")
    public FileWriter fileWriter() {
        return new FileWriter();
    }   
}
```

# Bean的四大条件注解

[SpringBoot基础篇Bean之@ConditionalOnBean与@ConditionalOnClass - 云+社区 - 腾讯云 (tencent.com)](https://cloud.tencent.com/developer/article/1403117)

```
@ConditionalOnBean         //	当给定的在bean存在时,则实例化当前Bean
@ConditionalOnMissingBean  //	当给定的在bean不存在时,则实例化当前Bean
@ConditionalOnClass        //	当给定的类名在类路径上存在，则实例化当前Bean
@ConditionalOnMissingClass //	当给定的类名在类路径上不存在，则实例化当前Bean
```

## @ConditionalOnBean

只有当另外一个实例存在时，才创建，否则不创建，也就是，最终有可能两个实例都创建了，有可能只创建了一个实例，也有可能一个实例都没创建。

该注解发生在spring ioc的`bean definition`阶段

来源：[条件注解 @ConditionalOnBean 的正确使用姿势 - 云+社区 - 腾讯云 (tencent.com)](https://cloud.tencent.com/developer/article/1449288)

不少人在使用 @ConditionalOnBean 注解时会遇到不生效的情况，依赖的 bean 明明已经配置了，但就是不生效。是不是`@ConditionalOnBean`和 Bean加载的顺序有没有关系呢？

问题演示：

```java
@Configuration
public class Configuration1 {

    @Bean
    @ConditionalOnBean(Bean2.class)
    public Bean1 bean1() {
        return new Bean1();
    }
}
```



```java
@Configuration
public class Configuration2 {

    @Bean
    public Bean2 bean2(){
        return new Bean2();
    }
}
```

**在`spring ioc`的过程中，优先解析`@Component，@Service，@Controller`注解的类。其次解析配置类，也就是`@Configuration`标注的类。最后开始解析配置类中定义的`bean`。** 

**示例代码中`bean1`是定义在配置类中的，当执行到配置类解析的时候，`@Component，@Service，@Controller ,@Configuration`标注的类已经全部扫描，所以这些`BeanDifinition`已经被同步。 但是`bean1`的条件注解依赖的是`bean2`，`bean2`是被定义的配置类中的，所以此时配置类的解析无法保证先后顺序，就会出现不生效的情况。**

**同样的道理，如果依赖的是`FeignClient`，可以设想一下结果？`FeignClient`最终还是由配置类触发的，解析的先后顺序同样也不能保证。**

解决

以下两种方式：

-  项目中条件注解依赖的类，大多会交给`spring`容器管理，所以如果要在配置中`Bean`通过`@ConditionalOnBean`依赖配置中的`Bean`时，完全可以用`@ConditionalOnClass(Bean2.class)`来代替。   
-  如果一定要区分两个配置类的先后顺序，可以将这两个类交与`EnableAutoConfiguration`管理和触发。也就是定义在`META-INF\spring.factories`中声明是配置类，然后通过`@AutoConfigureBefore、AutoConfigureAfter  AutoConfigureOrder`控制先后顺序。之所以这么做是因为这三个注解只对自动配置类的先后顺序生效。   这里推荐第一种。

## @ConditionalOnClass

要求class存在

先定义一个class

```java
public class DependedClz {
}
```

然后依赖class存在的bean

```java
public class LoadIfClzExists {
    private String name;

    public LoadIfClzExists(String name) {
        this.name = name;
    }

    public String getName() {
        return "load if exists clz: " + name;
    }
}
```

接下来就是Bean的配置

```java
/**
 * 当引用了 {@link DependedClz} 类之后，才会创建bean： `LoadIfClzExists`
 *
 * @return
 */
@Bean
@ConditionalOnClass(DependedClz.class)
public LoadIfClzExists loadIfClzExists() {
    return new LoadIfClzExists("dependedClz");
}
```

因为类存在，所以测试时，这个bean应该被正常注册

## @ConditionalOnMissingBean

和前面一个作用正好相反的，上面是要求存在bean，而这个是要求不存在

定义一个bean不存在时，才创建的bean：

```java
public class LoadIfBeanNotExists {
    public String name;

    public LoadIfBeanNotExists(String name) {
        this.name = name;
    }

    public String getName() {
        return "load if bean not exists: " + name;
    }
}
```

对应的bean配置如下:

```java
/**
 * 只有当没有notExistsBean时，才会创建bean: `LoadIfBeanNotExists`
 *
 * @return
 */
@Bean
@ConditionalOnMissingBean(name = "notExistsBean")
public LoadIfBeanNotExists loadIfBeanNotExists() {
    return new LoadIfBeanNotExists("notExistsBean");
}
```

因为没有notExistsBean，所以上面这个bean也应该被正常注册



## @ConditionalOnMissingClass

class不存在时，才会加载bean

定义一个class缺少时才会创建的bean

```java
public class LoadIfClzNotExists {
    private String name;

    public LoadIfClzNotExists(String name) {
        this.name = name;
    }

    public String getName() {
        return "load if not exists clz: " + name;
    }
}
```

bean的配置如下

```java
/**
 * 当系统中没有 com.example.depends.clz.DependedClz类时，才会创建这个bean
 *
 * @return
 */
@Bean
@ConditionalOnMissingClass("com.example.depends.clz.DependedClz")
public LoadIfClzNotExists loadIfClzNotExists() {
    return new LoadIfClzNotExists("com.example.depends.clz.DependedClz");
}
```

因为上面这个类存在，所以这个bean不应该被正常注册

# @Primary

当Spring容器扫描到某个接口的多个 bean 时，如果某个bean上加了@Primary 注解 ，则这个bean会被优先选用，如下面的例子：

```java
@Component
 public class FooService {

     private FooRepository fooRepository;

     @Autowired
     public FooService(FooRepository fooRepository) {
         this.fooRepository = fooRepository;
     }
 }

 @Component
 public class JdbcFooRepository extends FooRepository {

     public JdbcFooRepository(DataSource dataSource) {
         // ...
     }
 }

 @Primary
 @Component
 public class HibernateFooRepository extends FooRepository {

     public HibernateFooRepository(SessionFactory sessionFactory) {
         // ...
     }
 }
```

因为 HibernateFooRepository 上面有注解@Primary，所以它将优先JdbcFooRepository被注入到 FooService中。这在大量应用组件扫描时经常出现。
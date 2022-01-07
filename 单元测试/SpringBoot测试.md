SpringBoot Test 

https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html

## @RunWith和@SpringBootTest注解

https://blog.csdn.net/m0_38036104/article/details/109510905

**@SpringBootTest**

作用是加载ApplicationContext，启动spring容器。使用@SpringBootTest时并没有像@ContextConfiguration一样显示指定locations或classes属性，原因在于@SpringBootTest注解会自动检索程序的配置文件，检索顺序是从当前包开始，逐级向上查找@SpringBootApplication@SpringBootConfiguration注解的类。

常用的配置项如下：

value 指定配置属性
properties 指定配置属性，和value意义相同
classes 指定配置类，等同于@ContextConfiguration中的class，若没有显示指定，将查找嵌套的@Configuration类，然后返回到SpringBootConfiguration搜索配置
webEnvironment 指定web环境，可选值有：MOCK、RANDOM_PORT、DEFINED_PORT、NONE
webEnvironment详细说明：
MOCK 此值为默认值，该类型提供一个mock环境，此时内嵌的服务（servlet容器）并没有真正启动，也不会监听web端口。
RANDOM_PORT 启动一个真实的web服务，监听一个随机端口。 DEFINED_PORT
启动一个真实的web服务，监听一个定义好的端口（从配置中读取）。 NONE
启动一个非web的ApplicationContext，既不提供mock环境，也不提供真是的web服务

**@RunWith**

junit中的注解，当一个类用@RunWith注释或继承一个用@RunWith注释的类时，JUnit将调用它所引用的类来运行该类中的测试而不是开发者去在junit内部去构建它。

注意点：发现idea中springboot项目不加@RunWith仍然可以运行，所以比较疑问到底加不加，从网上获取到比较比较准确的说法如下：

标准测试类里是要有@RunWith的，作用是告诉java你这个类通过用什么运行环境运行，例如启动和创建spring的应用上下文。否则你需要为此在启动时写一堆的环境配置代码。你在IDEA里去掉@RunWith仍然能跑是因为在IDEA里识别为一个JUNIT的运行环境，相当于就是一个自识别的RUNWITH环境配置。但在其他IDE里并没有。
所以，为了你的代码能在其他IDE里边正常跑，建议还是加@RunWith

## MockMvc

干嘛的：测试Controller的,不启动项目就可以测试接口

文档：https://docs.spring.io/spring-framework/docs/current/reference/html/testing.html#spring-mvc-test-framework

https://blog.csdn.net/wo541075754/article/details/88983708

https://blog.csdn.net/qinwenjng120/article/details/107401954

MockMvc是由spring-test包提供，实现了对Http请求的模拟，能够直接使用网络的形式，转换到**Controller**的调用，使得测试速度快、不依赖网络环境。同时提供了一套验证的工具，结果的验证十分方便。

接口MockMvcBuilder，提供一个唯一的build方法，用来构造MockMvc。主要有两个实现：StandaloneMockMvcBuilder和DefaultMockMvcBuilder，分别对应两种测试方式，即独立安装和集成Web环境测试（并不会集成真正的web环境，而是通过相应的Mock API进行模拟测试，无须启动服务器）。MockMvcBuilders提供了对应的创建方法standaloneSetup方法和webAppContextSetup方法，在使用时直接调用即可。

步骤：

1、依赖：spring-boot-starter-test

2、Controller接口

```java
@RestController
public class HelloWorldController {

	@RequestMapping
	public String hello(String name){
		return "Hello " + name + "!";
	}
}

```



3、编写测试类。实例化MockMvc有两种形式，一种是使用StandaloneMockMvcBuilder，另外一种是使用DefaultMockMvcBuilder。测试类及初始化MockMvc初始化：

```java
//SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
@RunWith(SpringRunner.class)
//SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
@SpringBootTest
//测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
@WebAppConfiguration
public class HelloWorldTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		// 实例化方式一
		mockMvc = MockMvcBuilders.standaloneSetup(new HelloWorldController()).build();
		// 实例化方式二
//		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

```



单元测试方法：

```java
@Test
public void testHello() throws Exception {

	/*
	 * 1、mockMvc.perform执行一个请求。
	 * 2、MockMvcRequestBuilders.get("XXX")构造一个请求。
	 * 3、ResultActions.param添加请求传值
	 * 4、ResultActions.accept(MediaType.TEXT_HTML_VALUE))设置返回类型
	 * 5、ResultActions.andExpect添加执行完成后的断言。
	 * 6、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情
	 *   比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
	 * 7、ResultActions.andReturn表示执行完成后返回相应的结果。
	 */
	mockMvc.perform(MockMvcRequestBuilders
			.get("/hello")
			// 设置返回值类型为utf-8，否则默认为ISO-8859-1
			.accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
			.param("name", "Tom"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.content().string("Hello Tom!"))
			.andDo(MockMvcResultHandlers.print());
}

```

测试结果打印：

```
FlashMap:
       Attributes = null

MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [Content-Type:"application/json;charset=UTF-8", Content-Length:"10"]
     Content type = application/json;charset=UTF-8
             Body = Hello Tom!
    Forwarded URL = null
   Redirected URL = null
          Cookies = []
2019-04-02 21:34:27.954  INFO 6937 --- [       Thread-2] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'

```

整个过程如下：
1、准备测试环境
2、通过MockMvc执行请求
3、添加验证断言
4、添加结果处理器
5、得到MvcResult进行自定义断言/进行下一步的异步请求
6、卸载测试环境

注意事项：如果使用DefaultMockMvcBuilder进行MockMvc实例化时需在SpringBoot启动类上添加组件扫描的package的指定，否则会出现404。如：

```java
@ComponentScan(basePackages = "com.secbro2")
```

# swagger、PostMan测试

# 单元测试

https://blog.csdn.net/cicada_smile/article/details/117414067

- 可以测试controller接口
- 可以测试service接口
- 可以测试repository接口

方式：

- TestRestTemplate类

- HTTP工具类发HTTP请求

- MockMvc专门测试controller

  https://www.kancloud.cn/java-jdxia/java/1470325

  https://www.cnblogs.com/ifme/p/12671773.html

- Mockito测试

# 集成测试

https://www.kancloud.cn/java-jdxia/java/1470326





# JUnit4 与 JUnit5

JUnit4包：org.junit.vintage

 JUnit5包：org.junit.jupiter

当前Spring2.4.x以上使用的SpringBootTest不再兼容JUnit4，为了之后的升级方便，我们要使用 JUnit5

## JUnit4 与 JUnit 5 常用注解对比

https://blog.csdn.net/winteroak/article/details/80591598

|    JUnit4    |       JUnit5       |                           **说明**                           |
| :----------: | :----------------: | :----------------------------------------------------------: |
|    @Test     |       @Test        | 表示该方法是一个测试方法。JUnit5与JUnit 4的@Test注解不同的是，它没有声明任何属性，因为JUnit Jupiter中的测试扩展是基于它们自己的专用注解来完成的。这样的方法会被继承，除非它们被覆盖 |
| @BeforeClass |     @BeforeAll     | 表示使用了该注解的方法应该在当前类中所有使用了@Test @RepeatedTest、@ParameterizedTest或者@TestFactory注解的方法之前 执行； |
| @AfterClass  |     @AfterAll      | 表示使用了该注解的方法应该在当前类中所有使用了@Test、@RepeatedTest、@ParameterizedTest或者@TestFactory注解的方法之后执行； |
|   @Before    |    @BeforeEach     | 表示使用了该注解的方法应该在当前类中每一个使用了@Test、@RepeatedTest、@ParameterizedTest或者@TestFactory注解的方法之前 执行 |
|    @After    |     @AfterEach     | 表示使用了该注解的方法应该在当前类中每一个使用了@Test、@RepeatedTest、@ParameterizedTest或者@TestFactory注解的方法之后 执行 |
|   @Ignore    |     @Disabled      |                 用于禁用一个测试类或测试方法                 |
|  @Category   |        @Tag        | 用于声明过滤测试的tags，该注解可以用在方法或类上；类似于TesgNG的测试组或JUnit 4的分类。 |
| @Parameters  | @ParameterizedTest |                  表示该方法是一个参数化测试                  |
|   @RunWith   |    @ExtendWith     |    @Runwith就是放在测试类名之前，用来确定这个类怎么运行的    |
|    @Rule     |    @ExtendWith     | Rule是一组实现了TestRule接口的共享类，提供了验证、监视TestCase和外部资源管理等能力 |
|  @ClassRule  |    @ExtendWith     | @ClassRule用于测试类中的静态变量，必须是TestRule接口的实例，且访问修饰符必须为public。 |



# sqlite

xxx.sqlite放在resources/sqlite目录下

```yaml
spring:
  datasource:
    url: jdbc:sqlite::resourcesW:sqlite/xxx.sqlite
    driver-class-name: org.sqlite.JDBC
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      dialect: org.hibernate.dialect.SQLiteDialect
```



```
url: jdbc:sqlite:xxx.sqlite
```

xxx.sqlite放在当前项目根目录下


来源：

https://www.jianshu.com/p/1626dc9c63e7

https://vitzhou.gitbooks.io/junit5/content/junit/extension_model.html

https://dnocm.com/articles/cherry/junit-5-info/#%E6%89%A9%E5%B1%95

# 组合注解

在官方文档中，这部分与注解部分一同讲的，但我将它移到此处，因为绝大多数情况下，他都是与扩展API一同使用。

组合注解，顾名思义，当一个注解上存在其他的Junit注解时，同时也继承这些注解的语义

例如：组合Tag与Test注解

```java
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Tag("fast")
@Test
public @interface Fast {
}

```



```java
@Fast
void asserts() {
    assertTrue(true);
}
```



# Extend API

在 Junit5 中通过 `@ExtendWith` 注解实现添加扩展。

```java
@ExtendWith(DatabaseExtension.class)
public class SimpleTest {
  // code
}
```



```java
@Slf4j
public class DatabaseExtension implements BeforeAllCallback, AfterAllCallback {
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        log.info("连接数据库");
    }
    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        log.info("关闭数据库");
    }
}
```



@ExtendWith 提供了许多扩展的入口，具体的实现通过实现对应的接口，例如上面的 DatabaseExtension 实现 BeforeAllCallback，AfterAllCallback

## ExecutionCondition

定义执行条件，满足条件时才能执行，下面是一个例子

```java
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(PassConditionalExtension.class)
@Test
public @interface Pass {
    String value();
}
```





```java
public class PassConditionalExtension implements ExecutionCondition {
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        return AnnotationUtils.findAnnotation(context.getElement(), Pass.class)
                .map(Pass::value)
                .filter("我很帅"::equals)
                .map(item -> ConditionEvaluationResult.enabled("pass"))
                .orElse(ConditionEvaluationResult.disabled("pass is not okay!"));
    }
}
```



```java
public class ConditionalTest {
    @Pass("密码不对不执行")
    void notExec() {
        // code...
    }
    @Pass("我很帅")
    void exec() {
        // code...
    }
}
```

## TestInstanceFactory

定义测试实例，只能用于class上

## TestInstancePostProcessor

对测试实例处理，通常用于注入依赖

## TestInstancePreDestroyCallback

当测试实例销毁前调用

## ParameterResolver

处理参数，见下面例子

```java
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface BookInject {
    String title();
    int price() default 0;
}
```



```java
public class BookParameterResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.isAnnotated(BookInject.class);
    }
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.findAnnotation(BookInject.class)
                .map(book -> Book.of(book.title(), book.price()))
                .orElse(null);
    }
}
```



```java
@Slf4j
public class BookParameterTest {
    @Test
    @ExtendWith(BookParameterResolver.class)
    void exec(@BookInject(title = "删库") Book book) {
        log.info(book.toString());
    }
}
```



## TestWatcher

监听测试用例的执行结果

```java
@Slf4j
public class LogTestWatcher implements TestWatcher {
    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info("wow, 成功了！");
    }
    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        // 终止
    }
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        // 取消（跳过）
    }
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        // 失败
    }
}
```

## 生命周期回调

在一开始的例子中就是生命周期的回调，这里不写例子拉，他们执行的先后顺序如下

- BeforeAllCallback
  - BeforeEachCallback
    - BeforeTestExecutionCallback
    - AfterTestExecutionCallback
  - AfterEachCallback
- AfterAllCallback

## TestExecutionExceptionHandler

处理异常，如果存在一些自定义的运行时异常，这是很有用的，可以做些处理



```java
public class IgnoreExceptionExtension implements TestExecutionExceptionHandler {
    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (throwable instanceof Exception) {
            return;
        }
        throw throwable;
    }
}
```



```java
public class SimpleTest {
    @Test
    @ExtendWith(IgnoreExceptionExtension.class)
    void exec2() throws Exception {
        throw new Exception("被忽略");
    }
    @Test
    @ExtendWith(IgnoreExceptionExtension.class)
    void exec3() throws Throwable {
        throw new Throwable("不被忽略");
    }
}
```

## Intercepting Invocations

拦截测试方法，类似于 Spring 中的 AOP

```java
@Slf4j
@ExtendWith(MyInvocationInterceptorTest.LogInvocationInterceptor.class)
public class MyInvocationInterceptorTest {

    @ParameterizedTest
    @ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
    void showParameterized(String candidate) {
        log.error(candidate);
    }


    static class LogInvocationInterceptor implements InvocationInterceptor {
        @Override
        public void interceptTestTemplateMethod(Invocation<Void> invocation,
                                                ReflectiveInvocationContext<Method> invocationContext,
                                                ExtensionContext extensionContext) throws Throwable {
            Method executable = invocationContext.getExecutable();
            List<Object> arguments = invocationContext.getArguments();
            Class<?> targetClass = invocationContext.getTargetClass();
            log.info("executable method: " + executable.getName());
            log.info("arguments: " + arguments.stream().map(String::valueOf).collect(Collectors.joining()));
            log.info("targetClass: " + targetClass.getName());
            log.info("invocation.proceed() start");
            invocation.proceed();
            log.info("invocation.proceed() end");
        }
    }
}
```

InvocationInterceptor 中有多个方法 `interceptBeforeAllMethod` `interceptTestMethod` `interceptTestTemplateMethod` 等，分别在不同的时候拦截，里中 `@ParameterizedTest` 继承 `@TestTemplate` 所以使用 `interceptTestTemplateMethod`

拦截器中一般会传入这几个变量：

- invocation: 测试请求，只有`proceed()`代表执行
- invocationContext: 测试请求的上下文
- extensionContext: 扩展的上下文

## @SpringBootTest

注解中就使用到了@ExtendWith

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@ExtendWith({SpringExtension.class})
public @interface SpringBootTest {
    
}
```

其中SpringExtension实现了一堆回调接口：

```java
public class SpringExtension implements BeforeAllCallback, AfterAllCallback, TestInstancePostProcessor, BeforeEachCallback, AfterEachCallback, BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {
    //...
}
```


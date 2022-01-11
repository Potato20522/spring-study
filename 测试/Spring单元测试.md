# 简介

即使在Spring场景下的单元测试，也应该不依赖Spring容器，推荐的直接new一个对象。这样的前提是项目代码的分层架构要清晰，业务代码不要和依赖框架中的API，这样一个好处就是单元测试很方便，无需启动Spring容器。

此外，单元测试也不需要其他的任何基础设计，尤其是数据库访问，推荐的做法是直接对DAO层的接口返回直接Mock。

Spring全家桶也给提供了很多支持Mock的机制：

- Mock Environment
- Mock JNDI
- Mock Servlet API
- Mock Spring Web Reactive

## Mock Environment

查看Environment的概念: [Environment.md](../SpringBoot/spring-framework/Environment.md),如果点不开，就在本仓库里直接搜：Environment.md

```java
class SpringTestApplicationTests {
    static MockEnvironment environment;
    @BeforeAll
    static void  initMockEnvironment() {
        environment = new MockEnvironment();
        environment.setProperty("name","jack");
    }


    @Test
    void testEnvironment() {
        System.out.println(environment.getProperty("name"));
    }

}
```



或者这样：创建一个Bean,然后依赖注入MockEnvironment就行

```java
@Bean
MockEnvironment environment() {
  MockEnvironment environment = new MockEnvironment();
  environment.setProperty("name","jack");
  return environment;
}
```

## Mock JNDI



1

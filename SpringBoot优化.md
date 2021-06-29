环境：

- Window10
- Java8 -291，Java HotSpot
- SpringBoot 2.3.12

## 空项目内存占用对比

IDEA创建项目时依赖只勾选Spring Web

### SpringMVC

**1、打成jar包后，java -jar 运行**

内存占用结果：

- 183.5M

2、IDEA里运行

- 130M

### Spring WebFlux


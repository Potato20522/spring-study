# log4j

log4j1.x在2015年弃坑，转向log4j2了，最后版本时log4j1.2：https://logging.apache.org/log4j/1.2/

所以，在项目中，时常会看到这样的依赖：

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-log4j12</artifactId>
    <version>1.7.28</version>
</dependency>
```

这正是门面日志框架slf4j和log4j1.2相结合的依赖。

artifactId中不能写小数点，于是就写成了log4j12

# log4j2


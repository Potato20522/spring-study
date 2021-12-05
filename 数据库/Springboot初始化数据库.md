

参考文档：[“How-to” Guides (spring.io)](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization)

有如下几种方式：

## JPA

两个配置二选一

```properties
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update
```

## Hibernate

方式一：

```properties
spring.jpa.hibernate.ddl-auto=update
```
方式二：

```properties
spring.jpa.hibernate.ddl-auto=create
```

在classpath下建立：import.sql文件，程序启动时，会自动执行这个脚本里的建表语句

## 自动执行SQL脚本

如果在classpath 目录下有：schema.sql 和data.sql，Springboot会自动执行这两脚本，创建表和初始化数据

或者脚本命名为：

- schema-${platform}.sql
- data-${platform}.sql

platform 在如下配置指定：

```properties
spring.sql.init.platform=hsqldb, h2, oracle, mysql, postgresql等
```

还需要开启；

```properties
spring.sql.init.mode=always
```

这样的脚本会在JPA的EntityManagerFactory被创建之前执行

## 初始化Spring Batch 数据库

Spring Batch项目需要在数据库中建立几张表，如下设置就行：

```properties
spring.batch.jdbc.initialize-schema=always
```

## Flyway数据库迁移

```properties
spring.flyway.locations=classpath:db/migration,filesystem:/opt/migration
```

详情请看flyway文档：https://flywaydb.org/

## Liquibase 数据库迁移

详情请看Liquibase 文档：https://www.liquibase.org/
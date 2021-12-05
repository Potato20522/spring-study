# 简介

来源：https://blog.csdn.net/yanluandai1985/article/details/107388421

存储哪些内容？jobs，triggers，JobDataMap中的数据等

注意：不要在代码中直接new JobStore对象，Quartz会自动处理这些对象

## RAMJobStore

   RAMJobStore是使用最简单的JobStore，它也是性能最高的，因为它直接把Job执行过程中产生的数据存放到内存中。从源码上来看，发现了RAMJobStore中大量使用了HashMap类，所以在服务器宕机重启后，所有数据都丢失了。
        RAMJobStore基于内存，适合可以丢失数据的定时任务业务场景，优点在于它速度飞快。

在配置文件quartz.properties文件中这样配置

```properties
org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore
```

#### JDBCJobStore

  JDBCJobStore几乎与任何数据库一起使用，已被广泛应用于Oracle，PostgreSQL，MySQL，MS SQLServer，HSQLDB和DB2。Quartz提供了11张表，用于把JOB的数据存放在数据库中，能够在服务器宕机后恢复Job数据。
    对于多个调度程序实例，使用不同的前缀可能有助于创建多组表。

​    一般Mysql与Oracle使用标准的数据库方言。可以这样配置

```properties
org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate
```

 使用JDBCJobStore需要考虑使用什么事务。

   不需要将调度命令（例如添加和删除triggers）绑定到其他事务，那么可以通过使用JobStoreTX作为JobStore 来管理事务（这是最常见的选择）

```properties
org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX
```

  一般如果定时任务项目集成在Spring项目中，推荐使用JobStoreCMT - 在这种情况下，Quartz将让Spring容器来管理事务。

```properties
org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreCMT
```

​    JDBCJobStore可以自定义数据库连接，就像下面配置一样。也可以使用Spring容器集成得数据库连接

```properties
org.quartz.jobStore.dataSource=myDS
#============================================================================
# Configure Datasources  
#============================================================================
org.quartz.dataSource.myDS.driver:com.mysql.cj.jdbc.Driver
org.quartz.dataSource.myDS.URL:jdbc:mysql://localhost:3306/demo_quartz
org.quartz.dataSource.myDS.user:root
org.quartz.dataSource.myDS.password:3333
org.quartz.dataSource.myDS.maxConnections:5
org.quartz.dataSource.myDS.validationQuery:select 0

```

配置JDBCJobStore的表前缀

  默认的表前缀就是ORTZ\_，可以换成T\_QRTZ\_，但是要同步把建表SQL中的ORTZ_\的语句改为T\_QRTZ,配置就像下面一样就可以了。

```properties
org.quartz.jobStore.tablePrefix=QRTZ_
```


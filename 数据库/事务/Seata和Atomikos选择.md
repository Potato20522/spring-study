[整合Atomikos、Quartz、Postgresql的踩坑日记 (icode9.com)](https://www.icode9.com/content-2-948626.html)

由于业务需要，在单体Spring Boot项目中需要引入分布式事务，来保证单体应用连接的多个数据源的事务统一。

而说到分布式事务，小伙伴们肯定会想到阿里的Seata，阿里Seata强大的AT模式确实是解决分布式事务的一剂良药，

但是熟悉Seata的小伙伴肯定知道，**使用Seata需要单独搭建Seata服务端来支持分布式事务**，而对于一个**单体应用**项目有必要专门搭建这套服务端吗？





Seata使用限制

[SQL限制 (seata.io)](https://seata.io/zh-cn/docs/user/sqlreference/sql-restrictions.html)

- 不支持 SQL 嵌套
- 不支持多表复杂 SQL
- 不支持存储过程、触发器
- 不支持批量更新 SQL
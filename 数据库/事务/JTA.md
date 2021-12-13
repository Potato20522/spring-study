# 什么是JTA

https://www.jianshu.com/p/c16880f98fa3

JTA (Java Transaction API)  **Java事务API，是个标准**，**atomikos**、**Bitronix**、**Narayana**都是JTA的实现。JTA事务比JDBC事务更强大。一个JTA事务可以有多个参与者，而一个JDBC事务则被限定在一个单一的数据库连接。所以，当我们在同时操作多个数据库的时候，使用JTA事务就可以弥补JDBC事务的不足。

在Spring Boot 2.x中，整合了这两个JTA的实现：

- Atomikos：可以通过引入`spring-boot-starter-jta-atomikos`依赖来使用
- Bitronix：可以通过引入`spring-boot-starter-jta-bitronix`依赖来使用

Spring Boot通过Atomkos或Bitronix的内嵌事务管理器支持跨多个XA资源的分布式JTA事务，当部署到恰当的J2EE应用服务器时也会支持JTA事务。

当发现JTA环境时，Spring Boot将使用Spring的 JtaTransactionManager 来管理事务。自动配置的JMS，DataSource和JPA　beans将被升级以支持XA事务。可以使用标准的Spring idioms，比如 @Transactional ，来参与到一个分布式事务中。如果处于JTA环境，但仍想使用本地事务，你可以将 spring.jta.enabled 属性设置为 false 来禁用JTA自动配置功能。



# JTA中的对象

## UserTransaction接口

UserTransaction是Java EE中用来进行事务管理的一个接口

```java
package javax.transaction;

public interface UserTransaction {
    //开启一个事务
    void begin() throws NotSupportedException, SystemException;
		//提交当前事务
    void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException;
		//回滚当前事务
    void rollback() throws IllegalStateException, SecurityException, SystemException;
		//把当前事务标记为回滚
    void setRollbackOnly() throws IllegalStateException, SystemException;
		//获取事务的状态
    int getStatus() throws SystemException;
		//设置事务超时时间，超过就抛异常并回滚
    void setTransactionTimeout(int var1) throws SystemException;
}
```

## TransactionManager接口

```java
package javax.transaction;

public interface TransactionManager {
    void begin() throws NotSupportedException, SystemException;

    void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException;

    int getStatus() throws SystemException;
		//h
    Transaction getTransaction() throws SystemException;

    void resume(Transaction var1) throws InvalidTransactionException, IllegalStateException, SystemException;

    void rollback() throws IllegalStateException, SecurityException, SystemException;

    void setRollbackOnly() throws IllegalStateException, SystemException;

    void setTransactionTimeout(int var1) throws SystemException;

    Transaction suspend() throws SystemException;
}
```

XADataSource接口

XAConnection接口

XAResource

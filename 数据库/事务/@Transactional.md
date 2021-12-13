# @Transactional的实现原理

参考：

https://segmentfault.com/a/1190000022790265

https://cloud.tencent.com/developer/article/1562888

基本原理是：

将对应的方法通过注解元数据，标注在业务方法或者所在的对象上，然后在业务执行期间，**通过AOP拦截器反射读取元数据信息**，最终将根据读取的业务信息构建事务管理支持。

不同的方法之间的事务传播保证在同一个事务内，是通过**统一的数据源来实现的**，事务开始时**将数据源绑定到ThreadLocal**中，**后续加入的事务从ThreadLocal获取数据源来保证数据源的统一**。

## 1. 自动导入配置类

## 2. TransactionAutoConfiguration

### 2.1 按注解依次导入



### 2.2 AutoProxyRegistrar



### 2.3 ProxyTransactionManagementConfiguration



## 3. DataSourceTransactionManager


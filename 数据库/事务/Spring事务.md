# 注意点

事务，Spring AOP只在抛出RuntimeException时才回滚，不能try catch了异常

# JtaTransactionManager

构造函数传入两个对象：UserTransaction、TransactionManager
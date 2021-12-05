# 指定SpringBatch专用数据源

默认情况下，SpringBatch要求使用DataSource存储job的详细信息，我们需要给SpringBatch单独配置一个数据源，不要和业务的数据源混在一起，用以下两个注解指定SpringBatch专用数据源

- @Bean

- @BatchDataSource

## SpringBoot启动时执行Job

https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.batch.running-jobs-on-startup


# 学习项目搭建

IDEA新建Spring项目，依赖如下：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.2.0</version>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```



# HelloWorld

## 定义任务：

```java
@Slf4j
public class HelloJob implements Job {
    @SneakyThrows
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Thread.sleep(1000*3);
        log.info("Hello World!*************" + new Date());
    }
}
```

## 调度任务

```java
@Test
void helloWorld() throws SchedulerException {
    SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
    Scheduler scheduler = schedFact.getScheduler();
    scheduler.start();

    // 定义JobDetail
    JobDetail job = JobBuilder
            .newJob(HelloJob.class)
            .withIdentity("myJob", "group1")
            .build();

    // 设置触发器，立即执行，间隔5秒，重复0次（只会运行一次）
    Trigger trigger = TriggerBuilder
            .newTrigger()
            .withIdentity("myTrigger", "group1")
            .startNow()
            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                    .withIntervalInSeconds(5)
                    .withRepeatCount(0))
            .build();

    // 传入job和trigger
    scheduler.scheduleJob(job, trigger);
    //开启调度器
    scheduler.start();

    //主线程必须睡眠，不然直接没了
    try {
        // wait to show job
        Thread.sleep(30L * 1000L);
        // executing...
    } catch (Exception e) {
        //
    }
    //关闭调度器
    scheduler.shutdown();
}
```

## 结果日志

```
11:52:24.736 [main] INFO org.quartz.impl.StdSchedulerFactory - Using default implementation for ThreadExecutor
11:52:24.752 [main] INFO org.quartz.simpl.SimpleThreadPool - Job execution threads will use class loader of thread: main
11:52:24.752 [main] INFO org.quartz.core.SchedulerSignalerImpl - Initialized Scheduler Signaller of type: class org.quartz.core.SchedulerSignalerImpl
11:52:24.752 [main] INFO org.quartz.core.QuartzScheduler - Quartz Scheduler v.2.3.2 created.
11:52:24.752 [main] INFO org.quartz.simpl.RAMJobStore - RAMJobStore initialized.
11:52:24.768 [main] INFO org.quartz.core.QuartzScheduler - Scheduler meta-data: Quartz Scheduler (v2.3.2) 'DefaultQuartzScheduler' with instanceId 'NON_CLUSTERED'
  Scheduler class: 'org.quartz.core.QuartzScheduler' - running locally.
  NOT STARTED.
  Currently in standby mode.
  Number of jobs executed: 0
  Using thread pool 'org.quartz.simpl.SimpleThreadPool' - with 10 threads.
  Using job-store 'org.quartz.simpl.RAMJobStore' - which does not support persistence. and is not clustered.

11:52:24.768 [main] INFO org.quartz.impl.StdSchedulerFactory - Quartz scheduler 'DefaultQuartzScheduler' initialized from default resource file in Quartz package: 'quartz.properties'
11:52:24.768 [main] INFO org.quartz.impl.StdSchedulerFactory - Quartz scheduler version: 2.3.2
11:52:24.768 [main] INFO org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED started.
11:52:24.768 [DefaultQuartzScheduler_QuartzSchedulerThread] DEBUG org.quartz.core.QuartzSchedulerThread - batch acquisition of 0 triggers
11:52:24.768 [main] INFO org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED started.
11:52:24.768 [DefaultQuartzScheduler_QuartzSchedulerThread] DEBUG org.quartz.core.QuartzSchedulerThread - batch acquisition of 1 triggers
11:52:24.768 [DefaultQuartzScheduler_QuartzSchedulerThread] DEBUG org.quartz.simpl.PropertySettingJobFactory - Producing instance of Job 'group1.myJob', class=com.example.quartz.base.HelloJob
11:52:24.768 [DefaultQuartzScheduler_QuartzSchedulerThread] DEBUG org.quartz.core.QuartzSchedulerThread - batch acquisition of 0 triggers
11:52:24.768 [DefaultQuartzScheduler_Worker-1] DEBUG org.quartz.core.JobRunShell - Calling execute on job group1.myJob
11:52:27.768 [DefaultQuartzScheduler_Worker-1] INFO com.example.quartz.base.HelloJob - Hello World!*************Sat Dec 04 11:52:27 CST 2021
11:52:51.188 [DefaultQuartzScheduler_QuartzSchedulerThread] DEBUG org.quartz.core.QuartzSchedulerThread - batch acquisition of 0 triggers
11:52:54.776 [main] INFO org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED shutting down.
11:52:54.776 [main] INFO org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED paused.
11:52:54.776 [main] DEBUG org.quartz.simpl.SimpleThreadPool - Shutting down threadpool...
11:52:54.776 [main] DEBUG org.quartz.simpl.SimpleThreadPool - Shutdown of threadpool complete.
11:52:54.776 [main] INFO org.quartz.core.QuartzScheduler - Scheduler DefaultQuartzScheduler_$_NON_CLUSTERED shutdown complete.

```


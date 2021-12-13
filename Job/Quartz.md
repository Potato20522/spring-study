# Quartz

作业调度框架

Quartz 是一个完全由 Java 编写的开源作业调度框架，为在 Java 应用程序中进行作业调度提供了简单却强大的机制。

Quartz 可以与[ J2EE](https://www.w3cschool.cn/java_interview_question/java_interview_question-wvr326ra.html) 与 J2SE 应用程序相结合也可以单独使用。

Quartz 允许程序开发人员根据**时间的间隔**来调度作业。

Quartz 实现了作业和**触发器**的多对多的关系，还能把多个作业与不同的触发器关联。

## 核心概念

### Job

**Job** 表示一个被调度的任务，要执行的具体内容。此接口中只有一个方法，如下：

```java
public interface Job {
    void execute(JobExecutionContext var1) throws JobExecutionException;
}
```

主要有两种类型的 job：无状态的（stateless）和有状态的（stateful）。对于同一个 trigger 来说，有状态的 job 不能被并行执行，只有上一次触发的任务被执行完之后，才能触发下一次执行。Job 主要有两种属性：volatility 和 durability，其中 volatility 表示任务是否被持久化到数据库存储，而 durability 表示在没有 trigger 关联的时候任务是否被保留。两者都是在值为 true 的时候任务被持久化或保留。一个 job 可以被多个 trigger 关联，但是一个 trigger 只能关联一个 job。

```java
  public class HelloJob implements Job {

    public HelloJob() {
    }

    public void execute(JobExecutionContext context)
      throws JobExecutionException
    {
      System.err.println("Hello!  HelloJob is executing.");
    }
  }
```



### JobDetail

**JobDetail**  表示一个具体的可执行的调度程序，Job 是这个可执行程调度程序所要执行的内容，另外 JobDetail 还包含了这个任务**调度的方案和策略**。

说白了JobDetail就是调度之前声明的Job的

```java
  // define the job and tie it to our HelloJob class
  JobDetail job = newJob(HelloJob.class)
      .withIdentity("myJob", "group1") // name "myJob", group "group1"
      .build();
```



### Trigger

**Trigger** 代表一个调度参数的配置，什么时候去调，实现之一有CronTrigger，通过cron表达式的形式去指定执行策略。

```java

  // Trigger the job to run now, and then every 40 seconds
  Trigger trigger = newTrigger()
      .withIdentity("myTrigger", "group1")
      .startNow()
      .withSchedule(simpleSchedule()
          .withIntervalInSeconds(40)
          .repeatForever())            
      .build();

```



### Scheduler

**Scheduler** 代表一个调度容器，一个调度容器中可以注册多个 JobDetail 和 Trigger。当 Trigger 与 JobDetail 组合，就可以被 Scheduler 容器调度了。

```java
  // Tell quartz to schedule the job using our trigger
  sched.scheduleJob(job, trigger);
```



# HelloJob




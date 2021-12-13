package com.example.quartz.hello;

import org.junit.jupiter.api.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;


public class Demo1 {
    @Test
    void helloWorld() throws SchedulerException {
        SchedulerFactory schedFact = new StdSchedulerFactory();
        Scheduler scheduler = schedFact.getScheduler();
        // 定义JobDetail
        JobDetail job = JobBuilder
                .newJob(HelloJob.class)
                .withIdentity("myJob", "group1")
                .build();

        // 设置触发器，立即执行，间隔10秒，重复0次
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



}
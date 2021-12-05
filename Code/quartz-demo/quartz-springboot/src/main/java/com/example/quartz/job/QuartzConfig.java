package com.example.quartz.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @Description
 * @Author sgl
 * @Date 2018-06-26 16:45
 */
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail testQuartz1() {
        return JobBuilder.newJob(TestTask1.class)
                .withIdentity("testTask1") //Job Key: testTask1
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger testQuartzTrigger1() {
        //5秒执行一次
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        scheduleBuilder.withIntervalInSeconds(5);
        scheduleBuilder.withRepeatCount(1);
        return TriggerBuilder.newTrigger().forJob(testQuartz1())
                .withIdentity("testTask1") //Trigger Key:testTask1
                .withSchedule(scheduleBuilder)
//                .startNow()
                .build();
    }

    @Bean
    public JobDetail testQuartz2() {
        //Job Key: testTask1
        return JobBuilder.newJob(TestTask2.class).withIdentity("testTask2").storeDurably().build();
    }

    @Bean
    public Trigger testQuartzTrigger2() {
        //cron方式，每隔5秒执行一次
        return TriggerBuilder.newTrigger().forJob(testQuartz2())
                .withIdentity("testTask2")  //Trigger Key:testTask2
                .withSchedule(CronScheduleBuilder.cronSchedule("*/5 * * * * ?"))
                .build();
    }

}


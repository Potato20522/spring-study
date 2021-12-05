package com.example.quartz.hello;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

@Slf4j
public class HelloJob implements Job {
    @SneakyThrows
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Thread.sleep(1000*3);
        JobDataMap jobDataMap1 = context.getJobDetail().getJobDataMap();
        JobDataMap jobDataMap2 = context.getTrigger().getJobDataMap();
        log.info("Hello World!*************" + new Date());
    }
}
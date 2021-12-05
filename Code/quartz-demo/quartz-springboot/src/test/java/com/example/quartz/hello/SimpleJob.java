package com.example.quartz.hello;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import java.util.Set;

/**
 * Job的key
 */
@Slf4j
public class SimpleJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //从Job上下文中获取job的key(唯一标识)
        JobKey jobKey = context.getJobDetail().getKey();
        log.info("Executing job: " + jobKey +  "fired by: " + context.getTrigger().getKey());
        if(context.getMergedJobDataMap().size() > 0) {
            Set<String> keys = context.getMergedJobDataMap().keySet();
            for(String key: keys) {
                String val = context.getMergedJobDataMap().getString(key);
                log.info(" jobDataMap entry: " + key + " = " + val);
            }
        }

        context.setResult("hello");
    }
}

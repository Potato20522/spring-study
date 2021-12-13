package com.example.quartz.controller;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class JobController {
    @Autowired
    Scheduler scheduler;

    @Qualifier("testQuartzTrigger1")
    @Autowired
    Trigger trigger;

    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("job1")
    public String job1() {
        //手动触发
        try {
//            scheduler.triggerJob(jobDetail1.getKey());
            scheduler.scheduleJob(trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return "hello";
    }
}

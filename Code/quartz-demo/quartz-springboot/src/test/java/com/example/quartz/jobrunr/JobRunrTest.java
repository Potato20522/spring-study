package com.example.quartz.jobrunr;

import org.jobrunr.jobs.JobId;
import org.jobrunr.scheduling.BackgroundJob;
import org.jobrunr.scheduling.JobScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JobRunrTest {
//    @Autowired
//    private BackgroundJob backgroundJob;

    @Test
    public void hello(){
        BackgroundJob.enqueue(() -> System.out.println("hello"));
        System.out.println("zzzzz");
    }
}

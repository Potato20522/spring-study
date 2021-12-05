package com.example.quartz.job;

import org.springframework.stereotype.Component;

@Component
public class MyFirstJob {
    public void sayHello() {
        System.out.println("MyFirstJob: sayHello");
    }
}

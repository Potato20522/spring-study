package com.potato.springtest;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class EnvironmentDemo implements EnvironmentAware {
    @Override
    public void setEnvironment(Environment environment) {
        String property = environment.getProperty("project.name");
        System.out.println(property);
    }
}

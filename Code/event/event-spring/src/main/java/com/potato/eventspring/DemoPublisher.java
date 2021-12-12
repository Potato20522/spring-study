package com.potato.eventspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * 消息发布者
 */
@Component
public class DemoPublisher {
    @Autowired
    private ApplicationContext applicationContext;

    public void publish(long id, String message) {
        applicationContext.publishEvent(new DemoEvent(this, id, message));
    }

}

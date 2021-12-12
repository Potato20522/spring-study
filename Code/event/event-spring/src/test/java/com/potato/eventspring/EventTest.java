package com.potato.eventspring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EventTest {
    @Autowired
    DemoPublisher publisher;

    @Test
    void publisherTest() {
        publisher.publish(1L, "成功了");
    }
}

package com.potato.eventspring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsycDemoListener {
    @Async
    @EventListener
    public void asyncListener(DemoEvent demoEvent) {
        log.info("异步事件监听,当前线程:{},消息为:{}", Thread.currentThread().getName(), demoEvent.getMessage());
    }
}

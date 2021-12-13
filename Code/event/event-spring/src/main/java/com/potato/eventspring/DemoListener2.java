package com.potato.eventspring;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 监听器方式二: @EventListener
 */
@Component
public class DemoListener2 {

    @EventListener
    public void onApplicationEvent(DemoEvent demoEvent) {
        System.out.println(">>>>>>>>>DemoListener2>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("DemoListener2 收到了：" + demoEvent.getSource() + "消息;时间：" + demoEvent.getTimestamp());
        System.out.println("消息：" + demoEvent.getId() + ":" + demoEvent.getMessage());
    }
}


package com.potato.eventspring;


import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 监听器方式一：实现ApplicationListener
 */
@Component
public class DemoListener implements ApplicationListener<DemoEvent> {
    //参数为需要监听的事件类型
    @Override
    public void onApplicationEvent(DemoEvent demoEvent) {
        System.out.println(">>>>>>>>>DemoListener>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("DemoListener 收到了：" + demoEvent.getSource() + "消息;时间：" + demoEvent.getTimestamp());
        System.out.println("消息：" + demoEvent.getId() + ":" + demoEvent.getMessage());
    }
}
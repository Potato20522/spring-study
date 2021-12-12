package com.potato.eventjava.example1;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

public class Source {
    private int flag = 0;
    Set<EventListener> listeners = new HashSet<EventListener>();

    //注册事件监听器
    public void addStateChangeListener(StateChangeListener listener) {
        listeners.add(listener);
    }
    //注册事件监听器
    public void addStateChangeToOneListener(StateChangeToOneListener listener) {
        listeners.add(listener);
    }
    //当事件发生时，通知注册在事件源上的所有事件做出相应的反映
    public void notifyListener() {
        for (EventListener listener : listeners) {
            try {
                ((StateChangeListener)listener).handleEvent(new MyEvent(this));
            } catch (Exception e) {
                if (flag == 1) {
                    ((StateChangeToOneListener)listener).handleEvent(new MyEvent(this));
                }
            }
        }
    }
    //改变状态
    public void changeFlag() {
        flag = (flag == 0 ? 1 : 0);
        notifyListener();
    }

    public int getFlag() {
        return flag;
    }
}
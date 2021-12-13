package com.potato.eventjava.example2;

import java.util.EventObject;

/**
 * 定义事件对象，必须继承EventObject
 * 门的状态事件
 */
public class DoorEvent extends EventObject {

    private String doorState = "";// 表示门的状态，有“开”和“关”两种

    public DoorEvent(Object source, String doorState) {
        super(source);
        this.doorState = doorState;
    }

    public void setDoorState(String doorState) {
        this.doorState = doorState;
    }

    public String getDoorState() {
        return this.doorState;
    }

}
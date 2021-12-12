package com.potato.eventjava.example2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class DoorManager {
    private HashSet<DoorListener> listeners;

    /**
     * 添加事件
     * 
     * @param listener
     *            DoorListener
     */
    public void addDoorListener(DoorListener listener) {
        if (listeners == null) {
            listeners = new HashSet<>();
        }
        listeners.add(listener);
    }

    /**
     * 移除事件
     * 
     * @param listener
     *            DoorListener
     */
    public void removeDoorListener(DoorListener listener) {
        if (listeners == null)
            return;
        listeners.remove(listener);
    }

    /**
     * 触发开门事件
     */
    protected void fireWorkspaceOpened() {
        if (listeners == null)
            return;
        DoorEvent event = new DoorEvent(this, "open");
        notifyListeners(event);
    }

    /**
     * 触发关门事件
     */
    protected void fireWorkspaceClosed() {
        if (listeners == null)
            return;
        DoorEvent event = new DoorEvent(this, "close");
        notifyListeners(event);
    }

    /**
     * 通知所有的DoorListener
     */
    private void notifyListeners(DoorEvent event) {
        for (Object listener : listeners) {
            DoorListener doorListener = (DoorListener)listener;
            doorListener.doorEvent(event);
        }
    }
}
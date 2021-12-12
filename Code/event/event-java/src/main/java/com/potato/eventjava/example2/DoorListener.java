package com.potato.eventjava.example2;

import java.util.EventListener;

public interface DoorListener extends EventListener {
     public void doorEvent(DoorEvent event);
}
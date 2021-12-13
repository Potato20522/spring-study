package com.potato.eventspring;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class DemoEvent extends ApplicationEvent {
    private Long id;
    private String message;

    public DemoEvent(Object source, Long id, String message) {
        super(source);
        this.id = id;
        this.message = message;
    }
}
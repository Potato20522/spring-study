package com.potato.autocontroller.controller.hello;

import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService{
    @Override
    public String getHello() {
        return "hello world";
    }
}

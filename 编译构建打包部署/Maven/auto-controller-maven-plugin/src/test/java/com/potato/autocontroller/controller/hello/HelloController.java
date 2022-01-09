package com.potato.autocontroller.controller.hello;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HelloController {
    HelloService helloService;
    @GetMapping("hello")
    public String hello() {
        return helloService.getHello();
    }
}

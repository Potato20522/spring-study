package com.potato.springbatchdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    @Autowired

    @PostMapping
    public void hello() {

    }
}

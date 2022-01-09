package cn.tx.sboot.controller;

import cn.tx.sboot.model.Person;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class TestController {

    @RequestMapping("hello")
    public String hello(){
        return "hello springmvc";
    }

    @RequestMapping("test")
    public String test(){
        return "hello test";
    }

    @RequestMapping("selectPerson")
    public Person selectPerson(){
        Person p = new Person();
        p.setBirth(new Date());
        p.setGender(1);
        p.setPid(10);
        p.setPname("liangge");
        p.setPersonAddr("北京市昌平区");
        return p;
    }
}

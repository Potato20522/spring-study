package cn.tx.sboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Test1Controller {
    @RequestMapping("testview")
    public String testview(){
        return "index";
    }
}

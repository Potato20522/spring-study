package cn.tx.sboot.controller;

import cn.tx.sboot.model.AcmeProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@EnableConfigurationProperties(AcmeProperties.class)
public class YamlController {

    @Autowired
    private AcmeProperties properties;

    @GetMapping("test")
    public AcmeProperties test(){
        return properties;
    }


}

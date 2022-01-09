package cn.tx.sboot.controller;

import cn.tx.sboot.model.AcmeProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableConfigurationProperties(AcmeProperties.class)
public class YamlController {



    //@Autowired
    private AcmeProperties properties1;

    public YamlController(AcmeProperties properties1) {
        this.properties1 = properties1;
    }

    @GetMapping("test")
    public AcmeProperties test(){
        return properties1;
    }


}

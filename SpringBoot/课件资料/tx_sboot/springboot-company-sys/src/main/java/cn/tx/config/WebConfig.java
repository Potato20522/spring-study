package cn.tx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/header").setViewName("header");
        //registry.addViewController("/main").setViewName("main");
        registry.addViewController("/menu").setViewName("menu");
        registry.addViewController("/toSave").setViewName("save");
    }
}

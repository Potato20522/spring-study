package cn.tx.sboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticWebMVCConfig implements WebMvcConfigurer {


    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/txjava/**")
                .addResourceLocations("classpath:/mystatic/");
    }*/
}

package cn.tx.sboot.config;

import cn.tx.sboot.component.MyFilter;
import cn.tx.sboot.component.MyListener;
import cn.tx.sboot.component.MyServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class BootConfig {


    @Bean
    public ServletRegistrationBean myServlet(){
        ServletRegistrationBean srb = new ServletRegistrationBean(new MyServlet(), "/myServlet");
        return srb;
    }

    @Bean
    public FilterRegistrationBean myFilter(){
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new MyFilter());
        frb.setUrlPatterns(Arrays.asList("/myServlet", "/myFilter"));
        return frb;
    }

    @Bean
    public ServletListenerRegistrationBean myListener(){
        ServletListenerRegistrationBean slrb = new ServletListenerRegistrationBean(new MyListener());
        return slrb;
    }

}

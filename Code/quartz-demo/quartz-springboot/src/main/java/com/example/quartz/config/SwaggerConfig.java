package com.example.quartz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 *  在springboot 2.6.0结合swagger3.0.0会提示documentationPluginsBootstrapper NullPointerException，
 *  具体位置的WebMvcPatternsRequestConditionWrapper中的condition为null。
 *  原因是在springboot2.6.0中将SpringMVC 默认路径匹配策略从AntPathMatcher 更改为PathPatternParser，导致出错，解决办法是切换会原先的AntPathMatcher
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo())
                // 是否开启
                .enable(true).select()
                // 扫描的路径包
                .apis(RequestHandlerSelectors.basePackage("com.example.quartz.controller"))
                // 指定路径处理PathSelectors.any()代表所有的路径
                .paths(PathSelectors.any()).build().pathMapping("/").enable(true);
    }
 
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot-quartz")
                .description("springboot quartz")
                .version("1.0.0")
                .build();
    }
}
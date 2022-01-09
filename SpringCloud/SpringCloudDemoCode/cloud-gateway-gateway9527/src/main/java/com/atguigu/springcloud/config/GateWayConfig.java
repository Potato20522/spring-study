package com.atguigu.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateWayConfig {
    /**
     * 配置了一个id为router-name的路由规则
     * 当访问地址http://localhost:9527/guonei时会自动转发地址：http://news.baidu.com/guonei
     * @param routeLocatorBuilder
     * @return
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        //http://news.baidu.com/guonei
        routes.route("path_route_atguigu",
                r ->r.path("/guonei")
                .uri("http://news.baidu.com/guonei"))
                .build();
        return routes.build();
    }
    @Bean
    public RouteLocator customRouteLocator2(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        //http://news.baidu.com/guoji
        routes.route("path_route_atguigu2",
                r ->r.path("/guoji")
                        .uri("http://news.baidu.com/guoji"))
                .build();
        return routes.build();
    }
}

//package com.potato.securitywebsocket;
//
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//
//@Component
//@WebFilter(filterName = "WebsocketFilter", urlPatterns = "/*")
//public class WebsocketFilter implements Filter {
//    @Override
//    public void init(FilterConfig filterConfig){
//        System.out.println("执行filter");
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        System.out.println("doing Filter ... ");
//        response.setHeader("connectId","123456789");
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}
package cn.tx.sboot.component;

import javax.servlet.*;
import java.io.IOException;

public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("过滤器被初始化");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("发生过滤拦截");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("过滤器被销毁");
    }
}

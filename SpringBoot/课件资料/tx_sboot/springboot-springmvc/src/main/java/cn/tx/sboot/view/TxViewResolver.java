package cn.tx.sboot.view;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

public class TxViewResolver implements ViewResolver, Ordered {

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {

        System.out.println("解析视图"+ viewName);

        return new HelloView();
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE+10;
    }

}

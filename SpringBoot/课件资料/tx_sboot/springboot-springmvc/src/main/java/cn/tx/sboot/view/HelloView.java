package cn.tx.sboot.view;

import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class HelloView implements View {


    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        //2.让servlet用UTF-8转码
        response.setCharacterEncoding("UTF-8");

        response.getWriter().print("hello view: 自定义视图解析器测试");
    }

}

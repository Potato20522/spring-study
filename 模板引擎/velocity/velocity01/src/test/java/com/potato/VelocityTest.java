package com.potato;

import com.potato.pojo.User;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class VelocityTest {
    @Test
    void test01() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();
        context.put("name", "zhangsan");
        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/01-quickstart.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\01-quickstart.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test02() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();
        context.put("name", "zhangsan");
        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/02-cite-variable.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\02-cite-variable.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test03() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();
        User user = new User();
        user.setUsername("zhangsan");
        user.setPassword("123456");
        user.setEmail("zhangsan@163.com");
        context.put("user", user);
        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/03-cite-field.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\03-cite-field.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test04() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();
        context.put("str", "hello world velocity!");
        context.put("now", new Date());
        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/04-cite-method.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\04-cite-method.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test05() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();
        context.put("str", "hello world velocity!");
        context.put("now", new Date());
        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/05-instructions-set.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\05-instructions-set.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test06() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();
        context.put("str", "hello world velocity!");
        context.put("now", new Date());
        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/06-instructions-if.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\06-instructions-if.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test07() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();


        String[] hobbies = {"eat", "drink", "play", "happy"};
        context.put("hobbies", hobbies);

        List<User> users = new ArrayList<>();
        users.add(new User("aa","123456","123@163.com"));
        users.add(new User("bb","123456","456@163.com"));
        users.add(new User("bb","123456","789@163.com"));
        context.put("userList", users);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");
        context.put("map", map);

        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/07-instructions-foreach.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\07-instructions-foreach.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test08() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();


        String[] hobbies = {"eat", "drink", "play", "happy"};
        context.put("hobbies", hobbies);

        List<User> users = new ArrayList<>();
        users.add(new User("aa","123456","123@163.com"));
        users.add(new User("bb","123456","456@163.com"));
        users.add(new User("bb","123456","789@163.com"));
        context.put("userList", users);

        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        map.put("key3", "value3");
        map.put("key4", "value4");
        context.put("map", map);

        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/08-reference-include.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\08-reference-include.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test09() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();
        context.put("name", "zhangsan");
        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/09-reference-parse.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\09-reference-parse.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test10() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();
        context.put("name", "zhangsan");
        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/10-reference-define.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\10-reference-define.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test11() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();
        context.put("code", "#set($language=\"PHP\") #if($language.equals(\"JAVA\")) java开发工程师 #elseif($language.equals(\"PHP\")) php开发工程师 #else 开发工程师 #end\n");
        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/11-reference-evaluate.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\11-reference-evaluate.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }

    @Test
    void test12() throws IOException {
        //1.设置 velocity 的资源加载器
        Properties properties = new Properties();
        properties.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        //2.初始化 velocity 引擎
        Velocity.init(properties);
        //3.创建 velocity 容器
        VelocityContext context = new VelocityContext();
        List<User> users = new ArrayList<>();
        users.add(new User("aa","123456","123@163.com"));
        users.add(new User("bb","123456","456@163.com"));
        users.add(new User("bb","123456","789@163.com"));
        context.put("users", users);
        //4.加载 velocity 模板文件
        Template template = Velocity.getTemplate("vms/12-macro.vm", "utf-8");
        //5.合并数据到模板
        FileWriter fw = new FileWriter("E:\\JavaEE\\spring-study\\模板引擎\\velocity\\velocity01\\src\\main\\resources\\html\\12-macro.html");
        template.merge(context,fw);
        //6.释放资源
        fw.close();
    }
}

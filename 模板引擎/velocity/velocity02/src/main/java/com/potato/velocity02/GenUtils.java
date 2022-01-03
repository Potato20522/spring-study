package com.potato.velocity02;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.commons.lang3.*;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器   工具类
 */
public class GenUtils {

    private static String currentTableName;

    public static List<String> getTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("vms/Controller.java.vm");
        templates.add("vms/Service.java.vm");
        templates.add("vms/ServiceImpl.java.vm");
        templates.add("vms/Dao.java.vm");

        return templates;
    }


    /**
     * 生成代码
     */
    public static void generatorCode(Map<String, Object> data, List<String> templates, ZipOutputStream zip) {

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        //封装模板数据
        VelocityContext context = new VelocityContext(data);

        //获取模板列表
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, data.get("className").toString(), data.get("package").toString())));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    /**
     * 获取文件名 , 每个文件所在包都不一样, 在磁盘上的文件名几路径也各不相同
     */
    public static String getFileName(String template, String className,String packageName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator;
        }

        if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        return null;
    }
}
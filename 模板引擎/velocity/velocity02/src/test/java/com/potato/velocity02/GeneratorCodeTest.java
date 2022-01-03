package com.potato.velocity02;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;

public class GeneratorCodeTest {

    public static void main(String[] args) throws IOException {
        Map<String,Object> data = new HashMap<>();
        data.put("className","Product");
        data.put("classname","product");
        data.put("package","com.potato");

        File file = new File("C:\\Users\\20522\\Desktop\\code.zip");
        FileOutputStream outputStream = new FileOutputStream(file);
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        GenUtils.generatorCode(data,GenUtils.getTemplates(),zip);

        zip.close();
    }
}
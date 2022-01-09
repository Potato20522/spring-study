package com.potato.autocontroller.controller;

import com.potato.autocontroller.util.FileUtils;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class HelloControllerTest {
    @Test
    void generateHelloController() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        //构造类的基本信息
        cw.visit(V1_8, ACC_PUBLIC | ACC_SUPER, "sample/HelloController", null, "java/lang/Object", null);
        //添加@RestController注解
        cw.visitAnnotation("org/springframework/web/bind/annotation/RestController", true).visitEnd();

        //创建无参构造
        MethodVisitor mv1 = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv1.visitCode();//开始标识
        mv1.visitVarInsn(ALOAD, 0);
        mv1.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv1.visitInsn(RETURN);
        mv1.visitMaxs(1, 1);
        mv1.visitEnd();//结束标识

        //创建一个方法
        MethodVisitor m1 = cw.visitMethod(ACC_PUBLIC, "hello", "()Ljava/lang/String;", null, null);

        AnnotationVisitor getMappingAnnotation = m1.visitAnnotation("org/springframework/web/bind/annotation/GetMapping", true);
        AnnotationVisitor value = getMappingAnnotation.visitArray("value");
        value.visit(null, "hello");
        value.visitEnd();
        m1.visitCode();//开始标识
        m1.visitLdcInsn("hello world");
        m1.visitInsn(ARETURN);
        m1.visitMaxs(1,1);
        m1.visitEnd();//方法访问结束

        //类访问结束，输出二进制
        cw.visitEnd();
        byte[] bytes = cw.toByteArray();

        String relative_path = "sample/HelloController.class";
        String filepath = FileUtils.getFilePath(relative_path);
        FileUtils.writeBytes(filepath, bytes);
    }
}

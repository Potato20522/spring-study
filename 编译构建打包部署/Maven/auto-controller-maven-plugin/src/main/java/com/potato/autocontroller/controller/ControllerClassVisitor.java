package com.potato.autocontroller.controller;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.awt.font.OpenType;

/**
 * 构造Controller类
 */
public class ControllerClassVisitor extends ClassVisitor {

    public ControllerClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM9, classVisitor);
    }


}

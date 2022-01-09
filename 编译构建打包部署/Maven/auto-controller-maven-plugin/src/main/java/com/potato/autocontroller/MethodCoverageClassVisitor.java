package com.potato.autocontroller;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @author ironman
 */
public class MethodCoverageClassVisitor extends ClassVisitor {

    public MethodCoverageClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM9, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        final MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.contains("init")) {
            return methodVisitor;
        }

        return new MethodCoverageMethodVisitor(Opcodes.ASM9, methodVisitor);
    }
}
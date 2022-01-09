package com.potato.autocontroller;

import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

/**
 * @author ironman
 */
public class MethodCoverageMethodVisitor extends MethodVisitor {

    public MethodCoverageMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        System.out.printf("opcode:%s,owner:%s,name:%s,desc:%s,inInterface:%b\n", opcode, owner, name, descriptor,
                isInterface);
    }

    @Override
    public void visitCode() {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("method enter");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }
}
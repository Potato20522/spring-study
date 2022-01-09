package com.potato.autocontroller.controller;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;

import java.io.IOException;

import static org.objectweb.asm.Opcodes.ASM9;

public class ClassPrinterTest {
    @Test
    void visitHelloWorld() throws IOException {
        ClassPrinter p = new ClassPrinter(ASM9);
        ClassReader cr = new ClassReader("com/potato/autocontroller/controller/HelloWorld");
        cr.accept(p, 0);
    }
}

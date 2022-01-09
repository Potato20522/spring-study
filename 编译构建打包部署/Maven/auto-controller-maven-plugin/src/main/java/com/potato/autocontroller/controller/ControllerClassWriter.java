package com.potato.autocontroller.controller;

import org.objectweb.asm.ClassWriter;

public class ControllerClassWriter extends ClassWriter {

    public ControllerClassWriter() {
        super(ClassWriter.COMPUTE_FRAMES);
    }

}

package com.potato.autocontroller;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.*;

/**
 * Mojo注解+继承AbstractMojo: Maven插件的入口
 * 1.找到·
 */
@Mojo(name="touch",defaultPhase = LifecyclePhase.COMPILE)
public class AutoControllerMojo extends AbstractMojo {
    @Parameter(name="output",defaultValue = "${project.build.directory}")
    private File output;

    public void execute() throws MojoExecutionException {
        File f = output;

        if (!f.exists()) {
            f.mkdirs();
        }

        try {
            printFileName(f);
        } catch (Exception e) {
            throw new MojoExecutionException("instrument error", e);
        }
    }
    private void printFileName(File root) throws IOException {
        if (root.isDirectory()) {
            for (File file : root.listFiles()) {
                printFileName(file);
            }
        }

        if (root.getName().endsWith(".class")) {
            System.out.println(root.getName());
            FileOutputStream fos = null;
            try {
                final byte[] instrumentBytes = instrument(root);
                fos = new FileOutputStream(root);
                fos.write(instrumentBytes);
                fos.flush();
            } catch (MojoExecutionException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }
    private byte[] instrument(File clsFile) throws MojoExecutionException {
        try {
            ClassReader cr = new ClassReader(new FileInputStream(clsFile));
            ClassWriter cw = new ClassWriter(cr, ClassReader.SKIP_DEBUG);
            final ClassVisitor cv = new MethodCoverageClassVisitor(cw);
            cr.accept(cv, ClassReader.SKIP_DEBUG);

            return cw.toByteArray();
        } catch (Exception e) {
            throw new MojoExecutionException("instrument error", e);
        }
    }
}

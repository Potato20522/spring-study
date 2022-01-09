package com.potato.autocontroller.controller;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
 
public class ClassPrinter extends ClassVisitor {
	
	public ClassPrinter(int api) {
		super(api);
	}
 
	public void visit(int version, int access, String name, String signature,String superName, String[] interfaces) {
	    System.out.println("visit,version:"+version+",access:"+access+",name:"+name+",signature:"+signature+",superName:"+superName
	    		+",interfaces:"+interfaces);
	}
 
	public void visitSource(String source, String debug) {
	    System.out.println("visitSource,source:"+source + ",debug:"+debug);
	}
 
	public void visitOuterClass(String owner, String name, String desc) {
	    System.out.println("visitOuterClass,owner:"+owner + ",name:"+name+",desc:"+desc);
	}
	
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
	    	System.out.println("visitAnnotation,desc:"+desc + ",visible:"+visible);
		return null;
	}
 
	public void visitAttribute(Attribute attr) {
	    System.out.println("visitAttribute,attr:"+attr);
	}
 
	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
	    System.out.println("visitInnerClass,name:"+name+",outerName:"+outerName+",innerName:"+innerName+",access:"+access);
	}
 
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
	    	System.out.println("visitField,access:"+access+",name:"+name+",desc:"+desc+",signature:"+signature
	    		+",value:"+value);
		return null;
	}
 
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
	    System.out.println("visitMethod,access:"+access+",name:"+name+",desc:"+desc+",signature:"+signature
	    		+",exceptions:"+exceptions);
		return null;
	}
 
	public void visitEnd() {
		System.out.println("}");
	}

}
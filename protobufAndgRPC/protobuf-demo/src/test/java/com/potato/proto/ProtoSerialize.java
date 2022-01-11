package com.potato.proto;

import com.google.protobuf.Parser;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * 1101 1111
 */
public class ProtoSerialize {
    @Test
    void source() throws IOException {
        Source.Person.Builder person = Source.Person.newBuilder();
        for (int i = 0; i < 3; i++) {
            Point.Point3F.Builder point3f = Point.Point3F.newBuilder();
            point3f.setX(i + 0.1f);
            point3f.setY(i + 0.2f);
            point3f.setZ(i + 0.3f);
            person.addPoints(point3f);
        }
        byte[] bytes = person.build().toByteArray();
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));
        //[10, 15, 13, -51, -52, -52, 61, 21, -51, -52, 76, 62, 29, -102, -103, -103, 62, 10, 15, 13, -51, -52, -116, 63, 21, -102, -103, -103, 63, 29, 102, 102, -90, 63, 10, 15, 13, 102, 102, 6, 64, 21, -51, -52, 12, 64, 29, 51, 51, 19, 64]
        //位置 0000 1010
        FileOutputStream fos = new FileOutputStream("./source2");
        fos.write(bytes);
        fos.close();
    }

    @Test
    void source2() throws IOException{
        Source.Person person = Source.Person.parseFrom(new FileInputStream("./source"));
        System.out.println(person);
    }

    @Test
    void target() throws IOException{
        Target.Student.Builder student = Target.Student.newBuilder();
        for (int i = 0; i < 3; i++) {
            Point.Point3F.Builder point3f = Point.Point3F.newBuilder();
            point3f.setX(i + 0.1f);
            point3f.setY(i + 0.2f);
            point3f.setZ(i + 0.3f);
            student.addPoints(point3f);
        }
        byte[] bytes = student.build().toByteArray();
        System.out.println(bytes.length);
        System.out.println(Arrays.toString(bytes));
        //[18, 15, 13, -51, -52, -52, 61, 21, -51, -52, 76, 62, 29, -102, -103, -103, 62, 18, 15, 13, -51, -52, -116, 63, 21, -102, -103, -103, 63, 29, 102, 102, -90, 63, 18, 15, 13, 102, 102, 6, 64, 21, -51, -52, 12, 64, 29, 51, 51, 19, 64]
        //tag+类型 18： 0001 0010
        //后三位：010=2表示类型2，类型2有string,bytes,嵌套消息，repeated
        //余下前5位：0001 0 = 2，表示tag = 2
        FileOutputStream fos = new FileOutputStream("./student");
        fos.write(bytes);
        fos.close();
    }

    @Test
    void sourceToTarget()throws IOException{
        File file = new File("./source");
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        inputStream.read(bytes);
        System.out.println(Arrays.toString(bytes));

        //10-->18
        //0000 1010-->0001 0010
        byte b = bytes[0];
        bytes[0] = 18;
        Target.Student student = Target.Student.parseFrom(bytes);
        System.out.println(student);
    }


}

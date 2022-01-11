package com.potato.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import com.potato.proto.Point;
import com.potato.proto.Source;
import com.potato.proto.Target;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class RepeatedConverterTest {
    @Test
    void mapSingle() {
        //位置1的repeated属性,对应的tag是10
        byte[] bytes = new byte[]{10,0};
        RepeatedConverter.map(bytes, 2);
        System.out.println(Arrays.toString(bytes));
        //转成位置2的repeated属性后，tag变成18
        Assertions.assertEquals(18,bytes[0]);
    }

    @Test
    void map() throws InvalidProtocolBufferException {
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
        System.out.println("转换前：");
        System.out.println(Arrays.toString(bytes));
        RepeatedConverter.map(bytes,2);
        System.out.println("转换后：");
        System.out.println(Arrays.toString(bytes));
        Target.Student student = Target.Student.parseFrom(bytes);
        System.out.println(student);
    }
}
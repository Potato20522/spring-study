package com.potato;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PreconditionsTest {
    @Test
    void test01() {
        String param = "未读代码";
        String name = Preconditions.checkNotNull(param);
        System.out.println(name); // 未读代码
        String param2 = null;
        String name2 = Preconditions.checkNotNull(param2); // NullPointerException
        System.out.println(name2);
    }

    @Test
    void test02() {
        String param = "www.wdbyte.com2";
        String wdbyte = "www.wdbyte.com";
        Preconditions.checkArgument(wdbyte.equals(param), "[%s] 404 NOT FOUND", param);
// java.lang.IllegalArgumentException: [www.wdbyte.com2] 404 NOT FOUND
    }

    @Test
    void test03() {
        // Guava 中快速创建ArrayList
        List<String> list = Lists.newArrayList("a", "b", "c", "d");
// 开始校验
        int index = Preconditions.checkElementIndex(5, list.size());
// java.lang.IndexOutOfBoundsException: index (5) must be less than size (4)
    }
}

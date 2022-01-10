package com.potato.springtest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.env.MockEnvironment;

//@SpringBootTest
class SpringTestApplicationTests {
    static MockEnvironment environment;
    @BeforeAll
    static void  initMockEnvironment() {
        environment = new MockEnvironment();
        environment.setProperty("name","jack");
    }


    @Test
    void testEnvironment() {
        System.out.println(environment.getProperty("name"));
    }

}

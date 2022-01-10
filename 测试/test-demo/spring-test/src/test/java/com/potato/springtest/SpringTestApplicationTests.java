package com.potato.springtest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.env.MockEnvironment;

@SpringBootTest
class SpringTestApplicationTests {

    @Test
    void contextLoads() {
        MockEnvironment environment = new MockEnvironment();
//        environment.setProperty("name","jack");
//        environment.getProperty()
    }

}

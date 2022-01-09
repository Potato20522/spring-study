package cn.tx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.tx.mapper")
public class SysSpringApplication {


    public static void main(String[] args) {
        SpringApplication.run(SysSpringApplication.class);
    }
}

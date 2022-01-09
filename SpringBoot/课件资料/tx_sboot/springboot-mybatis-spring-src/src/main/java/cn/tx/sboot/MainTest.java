package cn.tx.sboot;

import cn.tx.sboot.config.MybatisSpringConfig1;
import cn.tx.sboot.mapper.PersonMapper;
import cn.tx.sboot.model.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainTest {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MybatisSpringConfig1.class);
        PersonMapper personMapper = (PersonMapper) ctx.getBean(PersonMapper.class);

        Person person = personMapper.selectById(1);
        System.out.println(person);

    }
}

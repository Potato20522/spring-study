package cn.tx.sboot.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
//在属性类中，前缀不可以驼峰模式，只能用羊肉串模式，但是在yml中是可以用驼峰模式来配置的
@ConfigurationProperties("acme.my-person.person")
@Component
@Validated
public class OwnerProperties {


    @NotNull
    private String firstName;

    @Max(35)
    private int age;

    //@Email
    private String email;


    @Valid
    private School school = new School();

    @Data
    class School{

        @NotNull
        private String sname;

    }



}

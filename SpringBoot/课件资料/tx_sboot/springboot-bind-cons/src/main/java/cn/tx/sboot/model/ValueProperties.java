package cn.tx.sboot.model;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@Validated
public class ValueProperties {

    @Value("${acme.my-person.person.first-name}")
    private String firstName;

    @Value("#{12*3}")
    private int age;

    @Email
    @Value("${acme.my-person.person.email}")
    private String email;

    @Value("${acme.favor}")
    private String favor ;

}

package cn.tx.sboot.model;

import lombok.Data;

@Data
public class User {

    private String name;

    private int age;


    private Role role;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }


}

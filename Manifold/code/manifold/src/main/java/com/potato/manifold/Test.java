package com.potato.manifold;

public class Test {
    public static void main(String[] args) {
        Person p1 = new Person("aa", 10);
        Person p2 = new Person("bb", 20);
        Person person = p1 + p2;
        System.out.println(person);
    }
}

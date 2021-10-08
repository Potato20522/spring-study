package com.potato.manifold;

public class GenericTest {
    static class MyClass<T>{
        public int a;
    }
    public static void main(String[] args) {
        MyClass<Double> myClass = new MyClass<Double>();
        System.out.println(myClass.getClass());

        MyClass<Person> myClass2 = new MyClass<Person>(){};
        MyClass<String> myClass3 = new MyClass<String>(){};
        System.out.println(myClass2.getClass());
        System.out.println(myClass3.getClass());
    }
}

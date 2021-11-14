package com.potato.nettywebsocket;

import org.junit.jupiter.api.Test;

public class Temp {
    @Test
    void test01(){
        int p1 = 100;
        int p2 = 100;

        for (int i = 0; i < 200; i++) {
            double random = Math.random();
            if (random>0.5){
                p1++;
                p2--;
            }else {
                p1--;
                p2++;
            }
            System.out.println(p1 - p2);
        }
    }
}

package com.jcore.util;

public class MathUtil {

    public static int random(int min, int max) {
        return (int) ((max + 1 - min) * Math.random() + min);
    }
    
    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            System.out.println(random(0, 6));
        }
    }

}

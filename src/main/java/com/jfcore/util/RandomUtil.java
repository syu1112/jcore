package com.jfcore.util;

import java.util.Random;

/**
 * @author xrbo
 */
public class RandomUtil {
    private static String digitalString = "0123456789";
    private static String wordString = "0GHI1KL2noCD789abcdefghiEFJM45NOpqrstUVuvwxyzABPQ36jklmRSTWXYZ";
    
    public static String digital(int size) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(digitalString.charAt(r.nextInt(10)));
        }
        return sb.toString();
    }
    
    public static String word(int size) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(wordString.charAt(r.nextInt(62)));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(RandomUtil.word(4));
        }
    }

}

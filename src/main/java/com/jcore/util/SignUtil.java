package com.jcore.util;

import com.jcore.util.codec.MD5Codec;

public class SignUtil {

    public static String sign(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string);
        }
        return MD5Codec.encode(sb.toString());
    }

    public static boolean validSign(String sign, String... values) {
        return sign(values).equals(sign);
    }

}

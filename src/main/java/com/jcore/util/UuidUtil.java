package com.jcore.util;

import java.util.UUID;

public class UuidUtil {

    public static String getUuidByJdk(boolean is32bit) {
        String uuid = UUID.randomUUID().toString();
        if (is32bit) {
            return uuid.toString().replace("-", "");
        }
        return uuid;
    }

    public static void main(String[] args) {
        System.out.println(getUuidByJdk(false));
        System.out.println(getUuidByJdk(true));
    }

}

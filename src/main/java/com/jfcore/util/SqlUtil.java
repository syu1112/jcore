package com.jfcore.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SqlUtil {

    public static String join(List<?> args) {
        if (args == null || args.size() == 0) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            Object arg = args.get(i);
            if (arg != null) {
                sb.append("'").append(arg).append("'");
            }
        }
        return sb.toString();
    }

    public static String join(Set<?> args) {
        if (args == null || args.size() == 0) {
            return "";
        }

        int i = 0;
        StringBuffer sb = new StringBuffer();
        for (Object arg : args) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("'").append(arg).append("'");
            ++i;
        }
        return sb.toString();
    }

    public static String join(Object... args) {
        if (args == null || args.length == 0) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            Object arg = args[i];
            if (arg != null) {
                sb.append("'").append(arg).append("'");
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(join("a", "b"));
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        System.out.println(join(list));
    }

}

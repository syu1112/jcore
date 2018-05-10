package com.jfcore.util;

import java.math.BigDecimal;

/**
 * @author xrbo
 */
public class ConvertUtil {

    public static String byte2String(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte c : data) {
            sb.append(c).append(",");
        }
        return sb.toString();
    }

    public static byte[] intToByte(int value) {
        byte b[] = new byte[4];
        for (int i = 0; i < 4; i++) {
            int offset = (b.length - 1 - i) * 8;
            b[i] = (byte) (value >>> offset & 255);
        }
        return b;
    }

    public static String byte2Hex(byte b[]) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 255;
            if (v < 16)
                sb.append('0');
            sb.append(Integer.toHexString(v));
        }

        return sb.toString().toUpperCase();
    }

    public static String string2Json(String string) {
        if (string == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {

            char c = string.charAt(i);
            switch (c) {
            case '\"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '/':
                sb.append("\\/");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\'':
                sb.append("\\'");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 从字符中解析Long
     *
     * @param value
     * @return
     */
    public static Long string2Long(String value) {
        Long rs = null;
        try {
            if (StringUtil.isNotEmpty(value)) {
                rs = Long.valueOf(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * 从字符中解析Integer
     *
     * @param value
     * @return
     */
    public static Integer string2Integer(String value) {
        Integer rs = null;
        try {
            if (StringUtil.isNotEmpty(value)) {
                rs = Integer.valueOf(value.split("\\.")[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static Short string2Short(String value) {
        Short rs = null;
        try {
            if (StringUtil.isNotEmpty(value)) {
                rs = Short.valueOf(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static Double string2Double(String value) {
        Double rs = null;
        try {
            if (StringUtil.isNotEmpty(value)) {
                rs = Double.valueOf(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static Float string2Float(String value) {
        Float rs = null;
        try {
            if (StringUtil.isNotEmpty(value)) {
                rs = Float.valueOf(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static BigDecimal string2BigDecimal(String value) {
        BigDecimal rs = null;
        try {
            if (StringUtil.isNotEmpty(value)) {
                rs = new BigDecimal(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static Byte string2Byte(String value) {
        Byte rs = null;
        try {
            if (StringUtil.isNotEmpty(value)) {
                rs = Byte.valueOf(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
}

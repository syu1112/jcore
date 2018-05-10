package com.jfcore.util.codec;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.jfcore.exception.RtException;

/**
 * @author xrbo
 */
public class SHA1Codec {
    private static MessageDigest digest = newDigest();

    private static MessageDigest newDigest() {
        try {
            return MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RtException("digest instance, SHA-1", e);
        }
    }

    public static String encode(String origin) {
        return byte2Hex(digest.digest(origin.getBytes()));
    }

    public static String encode(String origin, String charset) {
        try {
            return byte2Hex(digest.digest(origin.getBytes(charset)));
        } catch (UnsupportedEncodingException e) {
            throw new RtException("sha1, charset=" + charset, e);
        }
    }

    private static String byte2Hex(byte b[]) {
        StringBuffer sb = new StringBuffer(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 255;
            if (v < 16)
                sb.append('0');
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

}

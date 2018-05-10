package com.jfcore.util.codec;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.jfcore.exception.RtException;

public class AESUtil {
    private final static String algorithm = "AES";

    public static byte[] getRawKey() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance(algorithm);
            kgen.init(128);
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            return raw;
        } catch (NoSuchAlgorithmException e) {
            throw new RtException("key generator instance, " + algorithm, e);
        }

    }

    public static byte[] encode(String message, byte[] rawKey) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(rawKey, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RtException("encode cipher instance, " + algorithm, e);
        } catch (InvalidKeyException e) {
            throw new RtException("encode cipher init key", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RtException("encode cipher doFinal message", e);
        }
    }

    public static String decode(byte[] message, byte[] rawKey) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(rawKey, algorithm);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decrypted = cipher.doFinal(message);
            return new String(decrypted);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RtException("decode cipher instance, " + algorithm, e);
        } catch (InvalidKeyException e) {
            throw new RtException("decode cipher init key", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RtException("decode cipher doFinal message", e);
        }
    }

    public static void main(String[] args) throws Exception {
        String message = "abcde";
        byte[] rawKey = getRawKey(); // 随机生成密钥
        System.out.println("Key = " + rawKey);
        
        byte[] encrypted = encode(message, rawKey);
        System.out.println("org text = " + message); // 原数据
        System.out.println("encrypted = " + encrypted); // 加密以后
        System.out.println("          = "+Arrays.toString(encrypted).getBytes());
        
        String decrypted = decode(encrypted, rawKey); // 解密串
        System.out.println("decrypted = " + decrypted);
    }

}
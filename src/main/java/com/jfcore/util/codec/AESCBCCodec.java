package com.jfcore.util.codec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;

public class AESCBCCodec {
    public static void main(String args[]) throws Exception {
        String data = "Test String";
        System.out.println(data.length());
        String key = "1234567812345678";
        String iv = "1234567812345678";
        
        data = encrypt(data, key, iv);
        System.out.println(data);
        data = decrypt(data, key, iv).trim();
        System.out.println(data);
        System.out.println(data.length());
    }

    public static String encrypt(String data, String key, String iv) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            return new sun.misc.BASE64Encoder().encode(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String decrypt(String data, String key, String iv) throws Exception {
        return decrypt(new BASE64Decoder().decodeBuffer(data), key.getBytes(), iv.getBytes());
    }

    public static String decrypt(byte[] data, byte[] key, byte[] iv) throws Exception {
        try {
//            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(data);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(data);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    
}

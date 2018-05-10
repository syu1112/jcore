package com.jfcore.util.codec;

import com.jfcore.exception.RtException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author xrbo
 */
public class URLCodec {

    public static String encode(String str, String charset) {
        try {
            return URLEncoder.encode(str, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RtException("url encode, charset=" + charset, e);
        }
    }
    
    public static String decode(String str, String charset) {
        try {
            return URLDecoder.decode(str, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RtException("url decode, charset=" + charset, e);
        }
    }

}

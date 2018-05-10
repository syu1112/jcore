package com.jfcore.web.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonRtn<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;
    private T data;

    public JsonRtn() {
        this.code = -1;
        this.msg = "";
    }

    public JsonRtn(int code) {
        this.code = code;
    }

    public JsonRtn(String msg) {
        this.code = -1;
        this.msg = msg;
    }

    public JsonRtn(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonRtn(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public JsonRtn(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toJsonString() {
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }

}

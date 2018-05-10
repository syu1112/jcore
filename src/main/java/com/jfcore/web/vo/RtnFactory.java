package com.jfcore.web.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RtnFactory {
    public final static int code_succ = 1;
    public final static JsonRtn<?> succ = new JsonRtn<Object>(code_succ);
    public final static JsonRtn<?> fail = new JsonRtn<Object>(-1);
    public final static JsonRtn<?> invalid = new JsonRtn<Object>(-98, "验证错误");
    public final static JsonRtn<?> exception = new JsonRtn<Object>(-99, "系统错误");

    public static <T> JsonRtn<T> exception(String msg) {
        return new JsonRtn<T>(-99, msg);
    }

    public static boolean isSucc(JsonRtn<?> rtn) {
        if (rtn == null)
            return false;
        return rtn.getCode() == code_succ;
    }

    public static boolean isFail(JsonRtn<?> rtn) {
        if (rtn == null)
            return true;
        return rtn.getCode() != code_succ;
    }

    public static <T> JsonRtn<T> newSucc(T data) {
        return new JsonRtn<T>(code_succ, data);
    }

    public static <T> JsonRtn<T> newFail(String msg) {
        return new JsonRtn<T>(msg);
    }

    public static <T> JsonRtn<T> newFail(int code, String msg) {
        return new JsonRtn<T>(code, msg);
    }

    public static <T> JsonRtn<T> newException(String msg) {
        return new JsonRtn<T>(msg);
    }

    public static <T> JsonRtn<T> newCode(int code) {
        return new JsonRtn<T>(code);
    }

    @SuppressWarnings("unchecked")
    public static <T> JsonRtn<T> buildObj(Class<T> clazz, String jsonString) {
        JsonRtn<T> rtn = JSON.parseObject(jsonString, JsonRtn.class);
        if (isFail(rtn)) {
            return rtn;
        }
        rtn.setData(JSON.parseObject(rtn.getData().toString(), clazz));
        return rtn;
    }

    @SuppressWarnings("unchecked")
    public static <T> JsonRtn<List<T>> buildList(Class<T> clazz, String jsonString) {
        JsonRtn<List<T>> rtn = JSON.parseObject(jsonString, JsonRtn.class);
        if (isFail(rtn)) {
            return rtn;
        }
        rtn.setData(JSON.parseArray(rtn.getData().toString(), clazz));
        return rtn;
    }

    @SuppressWarnings("unchecked")
    public static <T> JsonRtn<Map<String, T>> buildMap(Class<T> clazz, String jsonString) {
        JsonRtn<Map<String, T>> rtn = JSON.parseObject(jsonString, JsonRtn.class);
        if (isFail(rtn)) {
            return rtn;
        }
        Map<String, T> map = new HashMap<String, T>();
        JSONObject jsonMap = JSON.parseObject(rtn.getData().toString());
        for (String key : jsonMap.keySet()) {
            map.put(key, JSON.parseObject(jsonMap.getString(key), clazz));
        }
        rtn.setData(map);
        return rtn;
    }

}

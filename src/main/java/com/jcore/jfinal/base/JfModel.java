package com.jcore.jfinal.base;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jcore.util.BeanUtil;
import com.jfinal.plugin.activerecord.Model;

public class JfModel<T extends JfModel<T>> extends Model<T> {
    private static final long serialVersionUID = 1L;
    
    public List<Integer> listInteger(List<T> list, String key) {
        List<Integer> result = new ArrayList<Integer>();
        for (T t : list) {
            result.add(t.getInt(key));
        }
        return result;
    }
    
    public List<String> listString(List<T> list, String key) {
        List<String> result = new ArrayList<String>();
        for (T t : list) {
            result.add(t.getStr(key));
        }
        return result;
    }
    
    public Set<String> hashString(List<T> list, String key) {
        Set<String> result = new HashSet<String>();
        for (T t : list) {
            result.add(t.getStr(key));
        }
        return result;
    }
    
    public <O> List<O> listVo(List<T> list, Class<O> clazz) {
        List<O> result = new ArrayList<O>();
        for (T t : list) {
            result.add(t.buildVo(clazz));
        }
        return result;
    }
    
    public <O> O buildVo(JfModel<T> model, Class<O> clazz) {
        if(model != null) {
            return model.buildVo(clazz);
        }
        return null;
    }
    

    public <O> O buildVo(Class<O> clazz) {
        try {
            Field[] fields = clazz.getDeclaredFields();
            if (fields != null && fields.length > 0) {
                O obj = clazz.newInstance();
                String name = "";
                Object value = null;
                for (int i = 0; i < fields.length; i++) {
                    name = fields[i].getName();
                    value = get(name);
                    if (value != null) {
                        BeanUtil.setProperty(obj, name, value);
                    }
                }

                return obj;
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <O> void buildModel(O obj) {
        if(obj == null) {
            return;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields != null && fields.length > 0) {
            String name = "";
            Object value = null;
            for (int i = 0; i < fields.length; i++) {
                name = fields[i].getName();
                value = BeanUtil.getProperty(obj, name);
                if (value != null) {
                    set(name, value);
                }
            }
        }
    }

}

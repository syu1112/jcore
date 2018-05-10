package com.jfcore.util;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanUtil {
    private final static Logger log = LoggerFactory.getLogger(BeanUtil.class);

    public static Object convertValue(Class<?> clazz, String value) {
        Object rs = null;
        if (clazz != null && value != null) {
            if (String.class.equals(clazz)) {
                rs = value;
            } else if (Integer.class.equals(clazz) || Integer.TYPE.equals(clazz)) {
                rs = ConvertUtil.string2Integer(value);
            } else if (Short.class.equals(clazz) || Short.TYPE.equals(clazz)) {
                rs = ConvertUtil.string2Short(value);
            } else if (Long.class.equals(clazz) || Long.TYPE.equals(clazz)) {
                rs = ConvertUtil.string2Long(value);
            } else if (Double.class.equals(clazz) || Double.TYPE.equals(clazz)) {
                rs = ConvertUtil.string2Double(value);
            } else if (Float.class.equals(clazz) || Float.TYPE.equals(clazz)) {
                rs = ConvertUtil.string2Float(value);
            } else if (BigDecimal.class.equals(clazz)) {
                rs = ConvertUtil.string2BigDecimal(value);
            } else if (Byte.class.equals(clazz) || Byte.TYPE.equals(clazz)) {
                rs = ConvertUtil.string2Byte(value);
            } else if (Date.class.equals(clazz)) {
                if (value.indexOf(":") != -1) {
                    rs = DateUtil.getDate(value, DateUtil.PATTEM_DATE_TIME);
                } else {
                    rs = DateUtil.getDate(value, DateUtil.PATTEM_DATE);
                }
            }
        }

        return rs;
    }

    /**
     * 设置bean的值 忽略大小写
     * 
     * @param bean
     *            目标bean
     * @param name
     *            field的名字
     * @param value
     *            要设置的值
     */
    public static void setProperty(Object bean, String name, Object value) {
        if (bean != null && name != null && value != null) {
            Class<?> clazz = bean.getClass();
            Method[] methods = clazz.getMethods();
            if (methods != null)
                for (Method method : methods) {
                    if (method.getName().startsWith("set") && name.toLowerCase().replaceAll("_", "").equals(method.getName().substring(3).toLowerCase().replaceAll("_", ""))) {
                        Class<?>[] types = method.getParameterTypes();
                        if (types != null && types.length > 0) {
                            Object val = convertValue(types[0], String.valueOf(value));
                            try {
                                method.invoke(bean, val);
                                break;
                            } catch (Exception e) {
                                log.error("setProperty-failed:" + name + "," + value, e);
                            }

                        }
                    }
                }
        }
    }

    /**
     * 获得bean的属性值，忽略大小写
     * 
     * @param bean
     *            bean实体
     * @param name
     *            field的名字
     * @return 属性值
     */
    public static Object getProperty(Object bean, String name) {
        if (bean != null && name != null) {
            Object rs = null;
            Class<?> clazz = bean.getClass();
            Method[] methods = clazz.getMethods();
            if (methods != null)
                for (Method method : methods) {
                    if (method.getName().startsWith("get") && name.toLowerCase().equals(method.getName().substring(3).toLowerCase())) {
                        try {
                            rs = method.invoke(bean);
                            break;
                        } catch (Exception e) {
                            log.error("getProperty-failed:" + name, e);
                        }
                    }
                }
            return rs;
        }
        return null;
    }
}

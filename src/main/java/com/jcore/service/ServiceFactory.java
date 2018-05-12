package com.jcore.service;


import java.util.HashMap;

import com.jcore.exception.RtException;
import com.jfinal.aop.Enhancer;

@SuppressWarnings("unchecked")
public class ServiceFactory {
    private static HashMap<Class<?>, Object> list = new HashMap<Class<?>, Object>();

    private static final String JAVABEAN_SUFFIX = "Impl";

    public static <T> T getService(Class<T> serviceClass) {

        if (serviceClass == null) {
            return null;
        }
        Object dao = list.get(serviceClass);
        if (dao == null) {
            dao = getSynchronizedService(serviceClass);
        }

        return dao == null ? null : (T) dao;
    }

    public static <T> T getImplService(Class<? extends T> implServiceClass) {
        if (implServiceClass == null) {
            return null;
        }

        Object dao = list.get(implServiceClass);
        if (dao == null) {
            dao = getSynchroizeImplService(implServiceClass);
        }

        return dao == null ? null : (T) dao;
    }

    private synchronized static Object getSynchronizedService(Class<?> daoClass) {
        Object dao = list.get(daoClass);
        if (dao == null) {
            dao = classInstance(daoClass.getName() + JAVABEAN_SUFFIX);
            list.put(daoClass, dao);
        }
        return dao;
    }

    private synchronized static Object getSynchroizeImplService(Class<?> implServiceClass) {
        Object obj = list.get(implServiceClass);
        if (obj == null) {
            obj = classInstance(implServiceClass);
            list.put(implServiceClass, obj);
        }
        return obj;
    }

    private static Object classInstance(final String className) {
        if (className == null || className.length() == 0) {
            return null;
        }
        try {
            return Enhancer.enhance(loadClass(className).newInstance());
        } catch (IllegalAccessException e) {
            throw new RtException("classInstance exception, className:" + className, e);
        } catch (InstantiationException e) {
            throw new RtException("classInstance exception, className:" + className, e);
        }
    }

    private static <T> T classInstance(Class<T> clazz) {
        try {
            return Enhancer.enhance(clazz.newInstance());
        } catch (IllegalAccessException e) {
            throw new RtException("classInstance exception, className:" + clazz.getName(), e);
        } catch (InstantiationException e) {
            throw new RtException("classInstance exception, className:" + clazz.getName(), e);
        }

    }

    @SuppressWarnings("rawtypes")
    private static Class loadClass(String className) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = ServiceFactory.class.getClassLoader();
            }
            Class classz = classLoader.loadClass(className);
            return classz;
        } catch (ClassNotFoundException e) {
            throw new RtException("loadClass exception, className:" + className, e);
        }
    }
}

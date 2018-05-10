package com.jfcore.jfinal.interceptor;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class LogInterceptor implements Interceptor {
    private static Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Override
    public void intercept(Invocation inv) {
        long starttime = new Date().getTime();
        logger.debug("START:{}/{}", inv.getControllerKey(), inv.getMethodName());
        inv.invoke();
        logger.debug("END:{}/{},time:{}ms", inv.getControllerKey(), inv.getMethodName(), new Date().getTime() - starttime);
    }

}

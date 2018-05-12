package com.jcore.web.listener;

import com.jcore.service.RedisServiceFactory;

import java.util.TimerTask;


public class RedisAliveTask extends TimerTask {

    private String env;
    
    public RedisAliveTask() {
        
    }

    public RedisAliveTask(String env) {
        this.env = env;
    }

    @Override
    public void run() {
        if (env == null) {
            RedisServiceFactory.getInstance().get("RedisAliveTask");
        } else {
            RedisServiceFactory.getInstance(env).get("RedisAliveTask");
        }
    }

}

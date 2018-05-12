package com.jcore.web.listener;

import java.util.TimerTask;

import com.jfinal.plugin.activerecord.Db;

public class DbAliveTask extends TimerTask {

    private String configName;
    
    public DbAliveTask() {
        
    }

    public DbAliveTask(String configName) {
        this.configName = configName;
    }

    @Override
    public void run() {
        if (configName == null) {
            Db.findFirst("select 1");
        } else {
            Db.use(configName).findFirst("select 1");
        }
    }

}

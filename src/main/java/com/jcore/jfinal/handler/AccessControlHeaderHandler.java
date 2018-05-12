package com.jcore.jfinal.handler;

import com.jfinal.ext.handler.AddHeaderHandler;

public class AccessControlHeaderHandler extends AddHeaderHandler {
    
    public AccessControlHeaderHandler() {
        this.addHeader("Access-Control-Allow-Origin", "*");
        this.addHeader("Access-Control-Allow-Headers", "X-Requested-With");
        this.addHeader("Access-Control-Allow-Methods", "GET, POST");
    }

}

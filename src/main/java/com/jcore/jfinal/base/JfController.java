package com.jcore.jfinal.base;

import java.awt.image.BufferedImage;

import com.jcore.util.StringUtil;
import com.jcore.util.WebUtil;
import com.jcore.web.vo.JsonRtn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.core.Controller;

public abstract class JfController extends Controller {
    protected static final Logger logger = LoggerFactory.getLogger(JfController.class);

    public void index() {
        renderError(403);
    }

    public String getRemoteIp() {
        return WebUtil.getRemoteIp(getRequest());
    }

    public int getRemotePort() {
        return getRequest().getRemotePort();
    }

    public String getHeaderReferer() {
        return getRequest().getHeader("referer");
    }

    public void renderRtn(JsonRtn<?> object) {
        renderJsonp(object.toJsonString());
    }

    public void renderJsonp(Object object) {
        String outputString = null;
        String rtn = getPara("rtn");
        if (StringUtil.isNotEmpty(rtn)) {
            outputString = " var " + rtn + "=" + object.toString() + ";";
        } else {
            String jsonCallback = getPara("jsoncallback");
            if (StringUtil.isNotEmpty(jsonCallback)) {
                outputString = jsonCallback + "(" + object.toString() + ");";
            } else {
                outputString = object.toString();
            }
        }
        renderJson(outputString);
    }

    public void renderJpeg(BufferedImage image) {
        renderImage(image, "jpeg");
    }

    public void renderPng(BufferedImage image) {
        renderImage(image, "png");
    }

    public void renderImage(BufferedImage image, String type) {
        WebUtil.renderImage(getRequest(), getResponse(), image, type);
        renderNull();
    }

}

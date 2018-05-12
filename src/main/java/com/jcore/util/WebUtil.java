package com.jcore.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jcore.web.vo.JsonRtn;
import com.jfinal.kit.LogKit;

public class WebUtil {

    public static String getRemoteIp(HttpServletRequest req) {
        String ip = req.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = req.getRemoteAddr();
        }
        return ip;
    }

    public static int getRemotePort(HttpServletRequest req) {
        return req.getRemotePort();
    }

    public static void renderRtn(HttpServletRequest req, HttpServletResponse resp, JsonRtn<?> rtn) {
        renderJsonp(req, resp, rtn.toJsonString());
    }

    public static void renderJsonp(HttpServletRequest req, HttpServletResponse resp, String outputString) {
        String rtn = req.getParameter("rtn");
        if (StringUtil.isNotEmpty(rtn)) {
            outputString = " var " + rtn + "=" + outputString + ";";
        } else {
            String jsonCallback = req.getParameter("jsoncallback");
            if (StringUtil.isNotEmpty(jsonCallback)) {
                outputString = jsonCallback + "(" + outputString + ");";
            }
        }
        renderJson(resp, outputString);
    }

    public static void renderJson(final HttpServletResponse resp, final Object obj) {
        try {
            resp.setContentType("text/javascript");
            resp.setCharacterEncoding("utf-8");
            disableCache(resp);
            resp.getWriter().write(obj.toString());
            resp.getWriter().flush();
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void renderImage(HttpServletRequest req, HttpServletResponse resp, BufferedImage image, String type) {
        resp.setContentType("image/" + type);
        ServletOutputStream sos = null;
        try {
            sos = resp.getOutputStream();
            ImageIO.write(image, type, sos);
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    LogKit.logNothing(e);
                }
            }
        }
    }

    public static void renderScript(HttpServletRequest req, HttpServletResponse resp, String msg) {
        try {
            disableCache(resp);
            resp.setContentType("text/html");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Cache-Control", "no-cache, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.getWriter().write("<script type=\"text/javascript\">document.domain='xunlei.com';" + msg + "</script>");
            resp.getWriter().flush();
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 回调页面中iframe的js，用于POST方式提交
     * 
     * @param req
     * @param resp
     * @param jsonstr
     */
    public static void renderIframeCallBack(HttpServletRequest req, HttpServletResponse resp, String jsonStr) {
        try {
            resp.setContentType("text/html");
            resp.setCharacterEncoding("utf-8");

            StringBuffer buf = new StringBuffer();
            buf.append("<html>");
            buf.append("<head>");
            buf.append("<script type=\"text/javascript\">\n");
            buf.append("document.domain = 'xunlei.com';\n");
            if (StringUtil.isNotEmpty(req.getParameter("jsoncallback"))) {
                buf.append("parent." + req.getParameter("jsoncallback") + "(" + jsonStr + ");\n");
            } else if (StringUtil.isNotEmpty((String) req.getAttribute("jsoncallback"))) {
                buf.append("parent." + req.getAttribute("jsoncallback").toString() + "(" + jsonStr + ");\n");
            }
            buf.append("</script>");
            buf.append("</head>");
            buf.append("<body></body>");
            buf.append("</html>");

            PrintWriter out = resp.getWriter();
            out.print(buf.toString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 禁用cache
     * 
     * @param resp
     */
    public static void disableCache(HttpServletResponse resp) {
        resp.setHeader("Pragma", "No-cache");
        resp.setDateHeader("Expires", 0);
        resp.setHeader("Cache-Control", "no-cache");
    }

    public static void setCookie(HttpServletResponse resp, String cookiename, String cookievalue) {
        setCookie(resp, cookiename, cookievalue, null, -1);
    }

    public static void setCookie(HttpServletResponse resp, String cookiename, String cookievalue, String domain, int expiretime) {
        Cookie cookie = new Cookie(cookiename, cookievalue);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(expiretime);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    public static void delCookie(HttpServletResponse resp, String cookiename, String domain) {
        Cookie cookie = new Cookie(cookiename, null);
        if (domain != null) {
            cookie.setDomain(domain);
        }
        cookie.setMaxAge(0);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest req, String cookiename) {
        Cookie[] cookies = req.getCookies();
        if (null != cookies && cookies.length > 0) {
            for (Cookie c : cookies) {
                if (c.getName().equals(cookiename)) {
                    return c.getValue();
                }
            }
        }
        return "";
    }

}

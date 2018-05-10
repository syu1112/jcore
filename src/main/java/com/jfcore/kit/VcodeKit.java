package com.jfcore.kit;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.jfcore.util.codec.MD5Codec;
import com.jfinal.kit.LogKit;

public class VcodeKit {
    public String cookie;
    public String md5key;
    public int width;
    public int height;
    public int size;
    public int timeout;
    
    public VcodeKit() {
        this.width = 90;
        this.height = 34;
        this.size = 4;
        this.timeout = 18000;
        this.md5key = "RyuvmIblxpLbQd3h";
        this.cookie = "VCODECK";
    }

    // 验证码随机字符数组
    private static final String[] strArr = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
    // 验证码字体
    private static final Font[] RANDOM_FONT = new Font[] { new Font("nyala", Font.BOLD, 38), new Font("Arial", Font.BOLD, 32), new Font("Bell MT", Font.BOLD, 32), new Font("Credit valley", Font.BOLD, 34), new Font("Impact", Font.BOLD, 32), new Font(Font.MONOSPACED, Font.BOLD, 40) };

    public void render(HttpServletResponse response, String vcode) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        drawGraphic(image, vcode);
        String code = MD5Codec.encode(vcode.toUpperCase());
//        pool.setObj(eCode, vCode); // 缓存，以后校验
        Cookie ck = new Cookie(cookie, code);
        ck.setMaxAge(timeout);
        ck.setPath("/");
        try {
            ck.setHttpOnly(true);
        } catch (Exception e) {
        }
        response.addCookie(ck);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        ServletOutputStream sos = null;
        try {
            sos = response.getOutputStream();
            ImageIO.write(image, "jpeg", sos);
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
    
    public String randVcode() {
        Random random = new Random();
        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String rand = String.valueOf(strArr[random.nextInt(strArr.length)]);
            sRand.append(rand);
        }
        return sRand.toString();
    }

    private String drawGraphic(BufferedImage image, String vcode) {
        // 获取图形上下文
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        // 图形抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 字体抗锯齿
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);

        // 生成随机类
        Random random = new Random();
        // 设定字体
        g.setFont(RANDOM_FONT[random.nextInt(RANDOM_FONT.length)]);

        // 画蛋蛋，有蛋的生活才精彩
        Color color;
        for (int i = 0; i < 10; i++) {
            color = getRandColor(120, 200);
            g.setColor(color);
            g.drawOval(random.nextInt(width), random.nextInt(height), 5 + random.nextInt(10), 5 + random.nextInt(10));
            color = null;
        }

        // 取随机产生的认证码(4位数字)
        StringBuilder sRand = new StringBuilder();
        int xoffset = width / vcode.length();
        for (int i = 0; i < size; i++) {
            String rand = vcode.substring(i, i+1);
            sRand.append(rand);
            // 旋转度数 最好小于45度
            int degree = random.nextInt(28);
            if (i % 2 == 0) {
                degree = degree * (-1);
            }
            // 定义坐标
            int x = xoffset * i, y = 21;
            // 旋转区域
            g.rotate(Math.toRadians(degree), x, y);
            // 设定字体颜色
            color = getRandColor(20, 130);
            g.setColor(color);
            // 将认证码显示到图象中
            g.drawString(rand, x + 4, y + 10);
            // 旋转之后，必须旋转回来
            g.rotate(-Math.toRadians(degree), x, y);
            color = null;
        }
//        StringBuilder sRand = new StringBuilder();
//        int xoffset = width / size;
//        for (int i = 0; i < size; i++) {
//            String rand = String.valueOf(strArr[random.nextInt(strArr.length)]);
//            sRand.append(rand);
//            // 旋转度数 最好小于45度
//            int degree = random.nextInt(28);
//            if (i % 2 == 0) {
//                degree = degree * (-1);
//            }
//            // 定义坐标
//            int x = xoffset * i, y = 21;
//            // 旋转区域
//            g.rotate(Math.toRadians(degree), x, y);
//            // 设定字体颜色
//            color = getRandColor(20, 130);
//            g.setColor(color);
//            // 将认证码显示到图象中
//            g.drawString(rand, x + 4, y + 10);
//            // 旋转之后，必须旋转回来
//            g.rotate(-Math.toRadians(degree), x, y);
//            color = null;
//        }
        // 图片中间线
        g.setColor(getRandColor(0, 60));
        // width是线宽,float型
        BasicStroke bs = new BasicStroke(3);
        g.setStroke(bs);
        // 画出曲线
        QuadCurve2D.Double curve = new QuadCurve2D.Double(0d, random.nextInt(height - 8) + 4, width / 2, height / 2, width, random.nextInt(height - 8) + 4);
        g.draw(curve);
        // 销毁图像
        g.dispose();
        return sRand.toString();
    }

    /*
     * 给定范围获得随机颜色
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}

package com.jfcore.jfinal.base;

import com.jfcore.jfinal.ext.AutoBindModels;
import com.jfcore.jfinal.ext.AutoBindRoutes;
import com.jfcore.jfinal.handler.AccessControlHeaderHandler;
import com.jfcore.jfinal.interceptor.ExceptionInterceptor;
import com.jfcore.jfinal.interceptor.LogInterceptor;
import com.jfinal.config.*;
import com.jfinal.ext.handler.CacheControlHeaderHandler;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext2.upload.filerenamepolicy.RandomFileRenamePolicy;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfinal.upload.OreillyCos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JfConfig extends JFinalConfig {
    private static Logger logger = LoggerFactory.getLogger(JfConfig.class);
    private String fileName = "jfinal.properties";

    @Override
    public void configConstant(Constants me) {
        // base
        me.setEncoding(PropKit.use(fileName).get("config.encoding", "UTF-8"));
        me.setMaxPostSize(PropKit.use(fileName).getInt("config.maxPostSize", 10485760));// 1024*1024*10=10M
        me.setBaseUploadPath(PropKit.use(fileName).get("config.baseUploadPath", "WEB-INF/upload"));
        // mode
        me.setDevMode(PropKit.use(fileName).getBoolean("config.devMode", false));
        logger.info("jfinal.devMode:{}", me.getDevMode());
        // view
        if (PropKit.use(fileName).getBoolean("config.viewMode", false)) {
            me.setViewType(ViewType.FREE_MARKER);
        } else {
            me.setViewType(ViewType.JFINAL_TEMPLATE);
        }
        me.setError401View("/error/401.html");
        me.setError403View("/error/403.html");
        me.setError404View("/error/404.html");
        me.setError500View("/error/500.html");
    }

    @Override
    public void configRoute(Routes me) {
        me.add(new AutoBindRoutes());
        logger.info("jfinal.AutoBindRoutes");
    }

    @Override
    public void configEngine(Engine engine) {

    }

    @Override
    public void configPlugin(Plugins me) {
        Prop prop = PropKit.use(fileName);
        String[] dbSources = prop.get("db.dataSource", "").split(",");
        for (String dataSource : dbSources) {
            if (dataSource == null || dataSource.trim().equals("")) {
                continue;
            }
            DruidPlugin druid = JfUtil.intDruid(dataSource);
            me.add(druid);

            ActiveRecordPlugin arp = new ActiveRecordPlugin(druid);
            arp.setShowSql(prop.getBoolean("config.sqlMode", prop.getBoolean("config.devMode", false)));
            me.add(arp);
            new AutoBindModels(dataSource, arp);
            logger.info("jfinal.AutoTableBindPlugin:{}", dataSource);
        }
    }

    @Override
    public void configInterceptor(Interceptors me) {
        me.add(new LogInterceptor());
        me.add(new ExceptionInterceptor());
    }

    @Override
    public void configHandler(Handlers me) {
        me.add(new CacheControlHeaderHandler(-1)); // cache control
        me.add(new AccessControlHeaderHandler()); // cross control
        me.add(new ContextPathHandler("cxt")); // web context
    }

    @Override
    public void afterJFinalStart() {
        super.afterJFinalStart();
        if (PropKit.use(fileName).getBoolean("config.uploadRename", true)) {
            OreillyCos.setFileRenamePolicy(new RandomFileRenamePolicy());
        }
//        Prop prop = PropKit.use("jfinal-weixin.properties");
//        // 配置微信 API 相关参数
//        ApiConfig ac = new ApiConfig();
//        ac.setAppId(prop.get("wx.appId"));
//        ac.setAppSecret(prop.get("wx.appSecret"));
//        ac.setToken(prop.get("wx.token", ""));
//        ac.setEncryptMessage(prop.getBoolean("wx.encryptMessage", false));
//        ac.setEncodingAesKey(prop.get("wx.encodingAesKey", ""));
//        ApiConfigKit.putApiConfig(ac);
//
//        //配置微信小程序 API 相关参数
//        WxaConfig wc = new WxaConfig();
//        wc.setAppId(prop.get("wxa.appId"));
//        wc.setAppSecret(prop.get("wxa.appSecret"));
//        wc.setToken(prop.get("wxa.token", ""));
//        wc.setMessageEncrypt(prop.getBoolean("wxa.messageEncrypt", false));
//        wc.setEncodingAesKey(prop.get("wxa.encodingAesKey", ""));
//        WxaConfigKit.setWxaConfig(wc);

        System.out.println(getBannerText());
    }

    @Override
    public void beforeJFinalStop() {
        super.beforeJFinalStop();
        System.out.println("jfcore stop!");
    }

    public static String getBannerText() {
        return   "     ___________  ___  ____\n" +
                " __ / / ___/ __ \\/ _ \\/ __/\n" +
                "/ // / /__/ /_/ / , _/ _/  \n" +
                "\\___/\\___/\\____/_/|_/___/  \n" +
                "                                ";
    }
}

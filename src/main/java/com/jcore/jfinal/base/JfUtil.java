package com.jcore.jfinal.base;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jcore.jfinal.ext.AutoBindModels;
import com.jcore.util.StringUtil;
import com.jcore.web.vo.JsonRtn;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.validate.Validator;

public abstract class JfUtil extends Validator {
    private static Logger logger = LoggerFactory.getLogger(JfUtil.class);

    private static Map<String, ActiveRecordPlugin> dbMap = new HashMap<String, ActiveRecordPlugin>();

    public static String fileName = "jfinal.properties";

    public static void renderRtn(Controller c, JsonRtn<?> rtn) {
        String callback = c.getPara("rtn");
        if (StringUtil.isNotEmpty(callback)) {
            c.renderJson("var " + callback + " = " + rtn.toJsonString() + ";");
            return;
        }

        callback = c.getPara("jsoncallback");
        if (StringUtil.isNotEmpty(callback)) {
            c.renderJson(callback + "(" + rtn.toJsonString() + ");");
            return;
        }

        c.renderJson(rtn.toJsonString());
    }

    public static void bindModel() {
        Prop prop = PropKit.use(fileName);
        for (String dataSource : prop.get("db.dataSource", "").split(",")) {
            if (dataSource == null || dataSource.trim().equals("")) {
                continue;
            }
            DruidPlugin druid = intDruid(dataSource);
            druid.start();

            ActiveRecordPlugin arp = new ActiveRecordPlugin(druid);
            arp.setShowSql(prop.getBoolean("config.sqlMode", prop.getBoolean("config.devMode", false)));
            new AutoBindModels(dataSource, arp);
            arp.start();
            dbMap.put(dataSource, arp);
        }
    }

    public static void unbindModel() {
        Prop prop = PropKit.use(fileName);
        for (String dataSource : prop.get("db.dataSource", "").split(",")) {
            if (dataSource == null || dataSource.trim().equals("")) {
                continue;
            }
            dbMap.get(dataSource).stop();
            logger.info("close dataSource:{}", dataSource);
        }
    }

    public static String[] findJars(String prefix) {
        URL classPathUrl = JfUtil.class.getResource("/");
        String libDir = new File(classPathUrl.getFile()).getParent();
        logger.info("find dir:{}", libDir);

        List<String> jars = new ArrayList<>();
        File baseDir = new File(libDir);
        if (!baseDir.exists()) {
            return null;
        }

        File[] files = baseDir.listFiles();
        for (File file : files) {
            if (file.getName().startsWith(prefix)) {
                jars.add(file.getName());
                logger.info("find jar:{}", file.getName());
            }
        }
        return jars.toArray(new String[jars.size()]);
    }

    public static DruidPlugin intDruid(String namePrefix) {
        Prop prop = PropKit.use(fileName);
        String url = prop.get(namePrefix + ".jdbcUrl").trim();
        String username = prop.get(namePrefix + ".userName").trim();
        String password = prop.get(namePrefix + ".passWord").trim();
        String driverClass = prop.get(namePrefix + ".driverClass").trim();

        DruidPlugin druid = new DruidPlugin(url, username, password, driverClass);
        druid.setInitialSize(prop.getInt("db.initialSize", 2));
        druid.setMinIdle(prop.getInt("db.minIdle", 2));
        druid.setMaxActive(prop.getInt("db.maxActive", 10));
        return druid;
    }

}

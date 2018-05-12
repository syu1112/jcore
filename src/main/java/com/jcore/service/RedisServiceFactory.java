package com.jcore.service;

import java.util.HashMap;
import java.util.Map;

import com.jcore.util.StringUtil;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class RedisServiceFactory {
    private static final Map<String, RedisService> instances = new HashMap<String, RedisService>();
    private static final Prop prop = PropKit.use("redis.properties");
    private static final String DEFAULT_ENV = "product";

    public synchronized static RedisService getInstance() {
        return getInstance(DEFAULT_ENV);
    }

    public synchronized static RedisService getInstance(String env) {
        if (StringUtil.isEmpty(env)) {
            env = DEFAULT_ENV;
        }

        RedisService instance = instances.get(env);
        if (instance != null) {
            return instance;
        }

        instance = new RedisService(initialPool(env));
        instances.put(env, instance);

        return instance;
    }

    private static JedisPool initialPool(String env) {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(prop.getInt(env + "_pool_max_total", config.getMaxTotal()));
        config.setMaxIdle(prop.getInt(env + "_pool_max_idle", config.getMaxIdle()));
        config.setMaxWaitMillis(prop.getLong(env + "_pool_max_wait", config.getMaxWaitMillis()));

        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);

        String host = prop.get(env + "_host", "localhost");
        int port = prop.getInt(env + "_port", 6379);
        String password = prop.get(env + "_password", "");
        int timeout = prop.getInt(env + "_pool_connection_timeout", 3000);

        if (StringUtil.isEmpty(password)) {
            return new JedisPool(config, host, port, timeout);
        } else {
            return new JedisPool(config, host, port, timeout, password);
        }
    }

    /**
     * lazy init singleton cache service
     */
    private static class RedisDefaultHolder {
        private static RedisService service = getInstance();
    }

    private static class RedisLocalHolder {
        private static RedisService service = getInstance("local");
    }

    private static class RedisTestHolder {
        private static RedisService service = getInstance("test");
    }

    public static RedisService getDefaulInstance() {
        return RedisDefaultHolder.service;
    }

    public static RedisService getLocalInstance() {
        return RedisLocalHolder.service;
    }

    public static RedisService getTestInstance() {
        return RedisTestHolder.service;
    }
}

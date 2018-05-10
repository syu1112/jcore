package com.jfcore.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import com.alibaba.fastjson.JSON;

public class RedisService {
    private JedisPool jedisPool;

    public RedisService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 获得jedis客户端
     * 
     * @return
     */
    public Jedis getJedisClient() {
        return jedisPool.getResource();
    }

    /**
     * 释放jedis到pool中
     * 
     * @param jedis
     */
    public void returnResource(Jedis jedis) {
        if (jedis != null && jedisPool != null) {
            jedisPool.returnResourceObject(jedis);
        }
    }

    /**
     * set key value
     * 
     * <pre>
     * 将字符串值 value 关联到 key
     * 如果 key 已经持有其他值， SET 就覆写旧值，无视类型
     * </pre>
     * 
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Jedis jedis = getJedisClient();
        try {
            jedis.set(key, value);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * setnx key value
     * 
     * <pre>
     * 将 key 的值设为 value ，当且仅当 key 不存在
     * 若给定的 key 已经存在，则 SETNX 不做任何动作
     * 不存在则保存成功返回1，存在或者异常则失败返回0
     * </pre>
     * 
     * @param key
     * @param value
     * @return
     */
    public long setnx(String key, String value) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.setnx(key, value);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * setex key seconds value
     * 
     * <pre>
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)
     * 如果 key 已经存在， SETEX 命令将覆写旧值
     * </pre>
     * 
     * @param key
     * @param value
     * @param seconds
     */
    public void setex(String key, String value, int seconds) {
        Jedis jedis = getJedisClient();
        try {
            jedis.setex(key, seconds, value);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * setex + setnx key seconds value
     * 
     * <pre>
     * 如果不存在key就添加并设置生存时间，单位秒
     * 如果存在key，就不做任何操作
     * 暂且不支持分布式
     * </pre>
     * 
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public synchronized void setexnx(String key, String value, int seconds) {
        Jedis jedis = getJedisClient();
        try {
            if (!jedis.exists(key)) {
                jedis.setex(key, seconds, value);
            }
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * get key
     * 
     * <pre>
     * 返回 key 所关联的字符串值
     * 如果 key 不存在那么返回 null
     * 假如 key 储存的值不是字符串类型返回 null
     * </pre>
     * 
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.get(key);
        } finally {
            returnResource(jedis);
        }
    }

    public void incrBatch(String prefix, List<String> keys) {
        Jedis jedis = getJedisClient();
        try {
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.incr(prefix + key);
            }
            p.sync();
        } finally {
            returnResource(jedis);
        }
    }

    public void decrBatch(String prefix, List<String> keys) {
        Jedis jedis = getJedisClient();
        try {
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.decr(prefix + key);
            }
            p.sync();
        } finally {
            returnResource(jedis);
        }
    }

    public void delBatch(String prefix, List<String> keys) {
        delBatch(prefix, keys.toArray(new String[] {}));
    }

    public void delBatch(String prefix, Set<String> keys) {
        delBatch(prefix, keys.toArray(new String[] {}));
    }

    public void delBatch(String prefix, String... keys) {
        Jedis jedis = getJedisClient();
        try {
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.del(prefix + key);
            }
            p.sync();
        } finally {
            returnResource(jedis);
        }
    }

    public void setBatch(String prefix, List<String> keys, String value) {
        Jedis jedis = getJedisClient();
        try {
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.set(prefix + key, value);
            }
            p.sync();
        } finally {
            returnResource(jedis);
        }
    }

    public void setBatch(String prefix, Set<String> keys, String value) {
        Jedis jedis = getJedisClient();
        try {
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                p.set(prefix + key, value);
            }
            p.sync();
        } finally {
            returnResource(jedis);
        }
    }

    public void setBatch(String prefix, Map<String, String> data) {
        Jedis jedis = getJedisClient();
        try {
            Pipeline p = jedis.pipelined();
            for (String key : data.keySet()) {
                p.set(prefix + key, data.get(key));
            }
            p.sync();
        } finally {
            returnResource(jedis);
        }
    }

    public Map<String, String> getBatch(String prefix, List<String> keys) {
        return getBatch(prefix, keys.toArray(new String[] {}));
    }

    public Map<String, String> getBatch(String prefix, Set<String> keys) {
        return getBatch(prefix, keys.toArray(new String[] {}));
    }

    public Map<String, String> getBatch(String prefix, String... keys) {
        Jedis jedis = getJedisClient();
        try {
            Map<String, Response<String>> resp = new HashMap<String, Response<String>>();
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                resp.put(key, p.get(prefix + key));
            }
            p.sync();

            Map<String, String> data = new HashMap<String, String>();
            for (String key : keys) {
                data.put(key, resp.get(key).get());
            }
            return data;
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * DEL key
     * 
     * <pre>
     * 删除给定的一个key，返回删除成功的个数， 也就是说，如果返回0表示删除失败，1表示删除成功
     * </pre>
     * 
     * @param key
     * @return
     */
    public long delete(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.del(key);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * EXISTS key
     * 
     * <pre>
     * 检查给定 key 是否存在
     * </pre>
     * 
     * @param key
     * @return
     */
    public boolean exists(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.exists(key);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * ttl key
     * 
     * <pre>
     * 以秒为单位，返回给定 key 的剩余生存时间(TTL, time to live)
     * </pre>
     * 
     * @param key
     * @return
     */
    public long ttl(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.ttl(key);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * INCR key
     * 
     * <pre>
     * 将 key 中储存的数字值增一
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作（返回1）
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么就抛出异常
     * </pre>
     * 
     * @param key
     * @return
     */
    public long incr(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.incr(key);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * INCRBY key increment
     * 
     * <pre>
     * 将 key 所储存的值加上增量 increment 。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * </pre>
     * 
     * @param key
     * @param increment
     * @return
     */
    public long incrBy(String key, long increment) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.incrBy(key, increment);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * DECR key
     * 
     * <pre>
     * 将 key 中储存的数字值减一
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECR 操作
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么就抛出异常
     * </pre>
     * 
     * @param key
     * @return
     */
    public long decr(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.decr(key);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 
     * DECRBY key decrement
     * 
     * <pre>
     * 将 key 所储存的值减去减量 decrement 。
     * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 DECRBY 操作。
     * 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。
     * </pre>
     * 
     * @param key
     * @param decrement
     * @return
     */
    public long decrBy(String key, long decrement) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.decrBy(key, decrement);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * set key object
     * 
     * <pre>
     * 使用fastjson序列化成String保存
     * </pre>
     * 
     * @param key
     * @param object
     */
    public void setObject(String key, Object object) {
        set(key, JSON.toJSONString(object));
    }

    /**
     * get key
     * 
     * <pre>
     * 获得object序列化的值再使用fastjson进行反序列化，进而返回clazz类型的对象
     * </pre>
     * 
     * @param key
     * @param clazz
     * @return
     */
    public <T> T getObject(String key, Class<T> clazz) {
        String data = get(key);
        return data == null ? null : JSON.parseObject(data, clazz);
    }

    /**
     * setnx key object
     * 
     * <pre>
     * 将 key 的值设为 value ，当且仅当 key 不存在
     * 若给定的 key 已经存在，则 SETNX 不做任何动作
     * 不存在则保存成功返回1，存在或者异常则失败返回0
     * </pre>
     * 
     * @param key
     * @param value
     * @return
     */
    public long setnxObject(String key, Object object) {
        return setnx(key, JSON.toJSONString(object));
    }

    /**
     * setex key seconds object
     * 
     * <pre>
     * 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)
     * 如果 key 已经存在， SETEX 命令将覆写旧值
     * </pre>
     * 
     * @param key
     * @param object
     * @param seconds
     */
    public void setexObject(String key, Object object, int seconds) {
        setex(key, JSON.toJSONString(object), seconds);
    }

    /**
     * setex + setnx key seconds value
     * 
     * <pre>
     * 如果不存在key就添加并设置生存时间，单位秒
     * 如果存在key，就不做任何操作
     * 暂且不支持分布式
     * </pre>
     * 
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public synchronized void setexnxObject(String key, Object object, int seconds) {
        setexnx(key, JSON.toJSONString(object), seconds);
    }

    @Override
    protected void finalize() throws Throwable {
        if (jedisPool != null) {
            jedisPool.destroy();
        }
    }

    /**
     * 头部加
     */
    public void lpush(String key, String... strings) {
        Jedis jedis = getJedisClient();
        try {
            jedis.lpush(key, strings);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 尾部加
     */
    public void rpush(String key, String... strings) {
        Jedis jedis = getJedisClient();
        try {
            jedis.rpush(key, strings);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 删除
     */
    public void lrem(String key, String value) {
        Jedis jedis = getJedisClient();
        try {
            jedis.lrem(key, 0, value);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 截取
     */
    public void ltrim(String key, long start, long end) {
        Jedis jedis = getJedisClient();
        try {
            jedis.ltrim(key, start, end);
        } finally {
            returnResource(jedis);
        }
    }

    public String lpop(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.lpop(key);
        } finally {
            returnResource(jedis);
        }
    }

    public String lindex(String key, long index) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.lindex(key, index);
        } finally {
            returnResource(jedis);
        }
    }

    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.lrange(key, start, end);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 长度
     */
    public long llen(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.llen(key);
        } finally {
            returnResource(jedis);
        }
    }

    public void sadd(String key, String... members) {
        Jedis jedis = getJedisClient();
        try {
            jedis.sadd(key, members);
        } finally {
            returnResource(jedis);
        }
    }

    public void srem(String key, String... members) {
        Jedis jedis = getJedisClient();
        try {
            jedis.srem(key, members);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 个数
     */
    public long scard(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.scard(key);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 是否存在
     */
    public boolean sismember(String key, String member) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.sismember(key, member);
        } finally {
            returnResource(jedis);
        }
    }

    public Set<String> smembers(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.smembers(key);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 差集
     */
    public Set<String> sdiff(String... keys) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.sdiff(keys);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 交集
     */
    public Set<String> sinter(String... keys) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.sinter(keys);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 并集
     */
    public Set<String> sunion(String... keys) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.sunion(keys);
        } finally {
            returnResource(jedis);
        }
    }

    public void zadd(String key, long score, String member) {
        Jedis jedis = getJedisClient();
        try {
            jedis.zadd(key, score, member);
        } finally {
            returnResource(jedis);
        }
    }

    public void zadd(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = getJedisClient();
        try {
            jedis.zadd(key, scoreMembers);
        } finally {
            returnResource(jedis);
        }
    }

    public void zrem(String key, String... members) {
        Jedis jedis = getJedisClient();
        try {
            jedis.zrem(key, members);
        } finally {
            returnResource(jedis);
        }
    }

    public Set<String> zrange(String key, long start, long end) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.zrange(key, start, end);
        } finally {
            returnResource(jedis);
        }
    }

    public Long zrank(String key, String member) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.zrank(key, member);
        } finally {
            returnResource(jedis);
        }
    }

    public Double zscore(String key, String member) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.zscore(key, member);
        } finally {
            returnResource(jedis);
        }
    }

    public void hset(String key, String field, String value) {
        Jedis jedis = getJedisClient();
        try {
            jedis.hset(key, field, value);
        } finally {
            returnResource(jedis);
        }
    }

    public void hmset(String key, Map<String, String> hash) {
        Jedis jedis = getJedisClient();
        try {
            jedis.hmset(key, hash);
        } finally {
            returnResource(jedis);
        }
    }

    public String hget(String key, String field) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.hget(key, field);
        } finally {
            returnResource(jedis);
        }
    }

    public List<String> hmget(String key, String... fields) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.hmget(key, fields);
        } finally {
            returnResource(jedis);
        }
    }

    public Map<String, String> hgetAll(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.hgetAll(key);
        } finally {
            returnResource(jedis);
        }
    }

    public Set<String> hkeys(String key) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.hkeys(key);
        } finally {
            returnResource(jedis);
        }
    }

    public long hincrBy(String key, String field, long value) {
        Jedis jedis = getJedisClient();
        try {
            return jedis.hincrBy(key, field, value);
        } finally {
            returnResource(jedis);
        }
    }

}

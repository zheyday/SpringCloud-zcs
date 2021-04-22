package zcs.commons.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    private DefaultRedisScript<Long> redisScript;
    private ValueOperations<String, Object> valueOperations;
    private HashOperations<String, Object, Object> hashOperations;
    private ListOperations<String, Object> listOperations;

    @Autowired
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        valueOperations = redisTemplate.opsForValue();
        hashOperations = redisTemplate.opsForHash();
        listOperations = redisTemplate.opsForList();
    }

    @PostConstruct
    public void init() {
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptText(
                "if redis.call('hexists', KEYS[2], KEYS[3]) == 1 then\n" +
                        "    --    用户已存在\n" +
                        "    return -2;\n" +
                        "end\n" +
                        "local number = tonumber(redis.call('get', KEYS[1]))\n" +
                        "if number <= 0 then\n" +
                        "    --    数量不足\n" +
                        "    return -1;\n" +
                        "end\n" +
                        "return redis.call('DECRBY', KEYS[1], 1)");
//        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("seckill.lua")));
    }

    /**
     * 设置生命周期
     *
     * @param key
     * @param time
     * @param unit
     */
    public void expire(String key, long time, TimeUnit unit) {
        redisTemplate.expire(key, time, unit);
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @return
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * String设置
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value) {
        try {
            valueOperations.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取key的值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        return key == null ? null : valueOperations.get(key);
    }

    /**
     * setnx 分布式锁
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public Boolean setnx(String key, Object value, long time) {
        return valueOperations.setIfAbsent(key, value, time, TimeUnit.MILLISECONDS);
    }

    /**
     * 解锁
     *
     * @param key
     * @return
     */
    public Boolean unlock(String key) {
        return this.delete(key);
    }

    /**
     * 乐观锁
     *
     * @param key
     * @return
     */
    public boolean watch(String key) {
        redisTemplate.watch(key);
        Integer number = (Integer) get(key);
        if (number <= 0) {
            return false;
        }
        redisTemplate.multi();
        valueOperations.decrement(key, 1);
        List<Object> list = redisTemplate.exec();
        return list.size() != 0;
    }

    /**
     * 结合lua脚本实现
     *
     * @param key0
     * @return
     */
    public Long lua(String key0, String key1, String key2) {
        List<String> keyList = new ArrayList<>();
        keyList.add(key0);
        keyList.add(key1);
        keyList.add(key2);
        List<String> valueList = new ArrayList<>();
        return redisTemplate.execute(redisScript, keyList);
//        return redisTemplate.execute((RedisCallback<Long>) connection -> {
//            Object nativeConnection = connection.getNativeConnection();
////            集群
//            if (nativeConnection instanceof JedisCluster)
//                return (Long) ((JedisCluster) nativeConnection).eval(redisScript.getScriptAsString(), keyList, valueList);
////            单点
//            else if (nativeConnection instanceof Jedis)
//                return (Long) ((Jedis) nativeConnection).eval(redisScript.getScriptAsString(), keyList, valueList);
//            else
//                return redisTemplate.execute(redisScript, keyList);
//        });
    }

    public Long handleStock(String goodId, String success, String componentKey) {
        Integer number = (Integer) valueOperations.get(goodId);
        if (number <= 0)
            return -1L;
        return decrBy(goodId, 1);
    }

    /**
     * 自增
     *
     * @param key
     * @param value
     */
    public Long incrBy(String key, Integer value) {
        return valueOperations.increment(key, value);
    }

    /**
     * 自减
     *
     * @param key
     * @param value
     * @return
     */
    public Long decrBy(String key, Integer value) {
        return valueOperations.decrement(key, value);
    }

    /**
     * 删除某个key
     *
     * @param key
     * @return
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个key
     *
     * @param keys
     * @return
     */
    public Long delete(Collection<String> keys) {
        return redisTemplate.delete(keys);
    }

    /**
     * 设置时间
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                valueOperations.set(key, value, time, TimeUnit.SECONDS);
                return true;
            } else
                return set(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //----------------------------   List操作 -------------------------------------
    public void lSet(String key, String value) {
        listOperations.rightPush(key, value);
    }

    //----------------------------   Hash操作 -------------------------------------
    public BoundHashOperations<String, Object, Object> boundHashOps(String key) {
        return redisTemplate.boundHashOps(key);
    }

    /**
     * 获取hash表中指定字段的值
     *
     * @param key
     * @param field
     * @return
     */
    public Object hGet(String key, String field) {
        return hashOperations.get(key, field);
    }

    /**
     * 将hash表key中的field的值设为value
     *
     * @param key
     * @param field
     * @param value
     */
    public void hPut(String key, String field, Object value) {
        hashOperations.put(key, field, value);
    }

    /**
     * 删除一个或多个字段
     *
     * @param key
     * @param fields
     * @return
     */
    public Long hDelete(String key, Object... fields) {
        return hashOperations.delete(key, fields);
    }

    /**
     * 是否存在field
     *
     * @param key
     * @param field
     * @return
     */
    public boolean hExists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }
}

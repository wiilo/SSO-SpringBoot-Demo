package com.wiilo.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis工具类
 *
 * @author Whitlock Wang
 * @date 2022/8/12 16:26
 */
@Slf4j
@Service("redisUtils")
public class RedisUtils {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 前缀
     */
    private static final String KEY_PREFIX_VALUE = "sso:certification:";
    private static final String KEY_PREFIX_SET = "sso:set:";
    private static final String KEY_PREFIX_LINKEDLIST = "sso:linkedList:";
    private static final String KEY_EMAIL = "sso:email:";

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheValue(String k, String v, long time) {
        return cacheValue(k, v, time, TimeUnit.SECONDS);
    }

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @param time
     * @param timeUnit
     * @return
     */
    public boolean cacheValue(String k, String v, long time, TimeUnit timeUnit) {
        String key = KEY_PREFIX_VALUE + k;
        try {
            ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
            valueOps.set(key, v);
            if (time > 0) {
                stringRedisTemplate.expire(key, time, timeUnit);
            }
            log.info("缓存[" + key + "]成功, value[" + v + "]");
            return true;
        } catch (Throwable t) {
            log.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 缓存value操作
     *
     * @param k
     * @param v
     * @return
     */
    public boolean cacheValue(String k, String v) {
        return cacheValue(k, v, -1);
    }

    /**
     * 判断缓存是否存在
     *
     * @param k
     * @return
     */
    public boolean containsValueKey(String k) {
        return containsKey(KEY_PREFIX_VALUE + k);
    }

    /**
     * 判断缓存是否存在
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        try {
            return stringRedisTemplate.hasKey(key);
        } catch (Throwable t) {
            log.error("判断缓存存在失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 获取缓存
     *
     * @param k
     * @return
     */
    public String getValue(String k) {
        try {
            ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
            return valueOps.get(KEY_PREFIX_VALUE + k);
        } catch (Throwable t) {
            log.error("获取缓存失败key[" + KEY_PREFIX_VALUE + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * 移除缓存
     *
     * @param k
     * @return
     */
    public boolean removeValue(String k) {
        return remove(initKey(k));
    }

    /**
     * 移除缓存
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        try {
            stringRedisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            log.error("获取缓存失败key[" + key + ", error[" + t + "]");
        }
        return false;
    }

    /**
     * 缓存set操作
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheSet(String k, String v, long time) {
        String key = KEY_PREFIX_SET + k;
        try {
            SetOperations<String, String> valueOps = stringRedisTemplate.opsForSet();
            valueOps.add(key, v);
            if (time > 0) {
                stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            log.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 获取list缓存
     *
     * @param k
     * @return
     */
    public List<String> getLinkedList(String k) {
        return getLinkedList(k, 0L, null);
    }

    /**
     * 获取list缓存
     *
     * @param k
     * @return
     */
    public List<String> getLinkedList(String k, Long start, Long end) {
        String key = KEY_PREFIX_LINKEDLIST + k;
        try {
            ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
            if (start == 0L && end == null) {
                end = listOps.size(key);
            }
            return listOps.range(key, start, end);
        } catch (Throwable t) {
            log.error("获取list缓存失败key[" + KEY_PREFIX_LINKEDLIST + k + ", error[" + t + "]");
        }
        return null;
    }

    /**
     * list缓存
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    public boolean cacheLinkedList(String k, String v, long time) {
        String key = KEY_PREFIX_LINKEDLIST + k;
        try {
            ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
            listOps.rightPush(key, v);
            if (time > 0) {
                stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            log.error("缓存[" + key + "]失败, value[" + v + "]", t);
        }
        return false;
    }

    /**
     * 获取总条数, 可用于分页
     *
     * @param k
     * @return
     */
    public long getLinkedListSize(String k) {
        try {
            ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
            return listOps.size(KEY_PREFIX_LINKEDLIST + k);
        } catch (Throwable t) {
            log.error("获取list长度失败key[" + KEY_PREFIX_LINKEDLIST + k + "], error[" + t + "]");
        }
        return 0;
    }

    /**
     * 删除并返回第一个
     *
     * @param k
     * @return
     */
    public String removeLinkedListFirst(String k) {
        try {
            ListOperations<String, String> listOps = stringRedisTemplate.opsForList();
            return listOps.leftPop(KEY_PREFIX_LINKEDLIST + k);
        } catch (Throwable t) {
            log.error("获取list长度失败key[" + KEY_PREFIX_LINKEDLIST + k + "], error[" + t + "]");
        }
        return null;
    }

    /***
     * redis lock on (key : {@code key} , value : {@code value})
     * @param key
     * @param value 当前时间+超时时间
     * @return 锁住返回true
     */
    public boolean lock(String key, String value) {
        return lockTime(key, value, null);
    }

    /***
     * redis unlock on (key : {@code key} , value : {@code value})
     * @param key
     * @param value
     * @return
     */
    public void unlock(String key, String value) {
        log.info("redis unlock on (key : {})", key);
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            log.error("unlock error, msg : {}", e);
        }
    }

    public boolean lockTime(String key, String value, Long time) {
        log.info("redis add lock on (key : {} , value : {} , time : {})", key, value, time);
        try {
            if (time == null) {
                time = -1L;
            }
            if (stringRedisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS)) {
                log.info("locked success");
                return true;
            }
        } catch (Exception e) {
            log.error("locked error, msg : {}", e);
        }
        log.info("locked fail");
        return false;
    }

    /**
     * 获取唯一 Id
     *
     * @param redisKey
     * @return
     */
    public Long increment(String redisKey) {
        return stringRedisTemplate.opsForValue().increment(redisKey, 1L);
    }

    /**
     * 处理中邮件发送记录
     *
     * @param order    母订单号
     * @param order_sn 子订单号
     * @return
     */
    public void disposeEmail(String order, String order_sn) {
        HashOperations<String, Object, Object> opsForHash = stringRedisTemplate.opsForHash();
        opsForHash.put(KEY_EMAIL + order, order_sn, order_sn);
        stringRedisTemplate.expire(KEY_EMAIL + order, 60 * 60 * 24 * 60, TimeUnit.SECONDS);
    }

    /**
     * 获取已经发送的邮件的订单
     *
     * @param order
     * @return
     */
    public List<Object> isDisposeEmail(String order) {
        return stringRedisTemplate.opsForHash().values(KEY_EMAIL + order);
    }

    public boolean isDisposeEmail(String order, String order_sn) {
        Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(KEY_EMAIL + order);
        if (!map.isEmpty() && map.containsKey(order_sn)) {
            return true;
        }
        return false;
    }

    /**
     * 标识一次性邮件发货标识
     *
     * @param order
     */
    public void setMailFlag(String business, String order) {
        SetOperations<String, String> operations = stringRedisTemplate.opsForSet();
        operations.add(business, order);
        stringRedisTemplate.expire(KEY_EMAIL + order, 60 * 60 * 24 * 60, TimeUnit.SECONDS);
    }

    public boolean getMailFlag(String business, String order_id) {
        Set<String> set = stringRedisTemplate.opsForSet().members(business);
        if (CollectionUtils.isNotEmpty(set) && set.contains(order_id)) {
            return true;
        }
        return false;
    }

    public String getGpValue(String k) {
        try {
            ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
            return valueOps.get(k);
        } catch (Exception e) {
            log.error("获取缓存失败key：{} 异常：{}", k, e);
        }
        return null;
    }

    public Long getExpirationTime(String k) {
        try {
            return stringRedisTemplate.opsForValue().getOperations().getExpire(KEY_PREFIX_VALUE + k);
        } catch (Exception e) {
            log.error("获取ExpirationTime失败key：{}  异常：{}", KEY_PREFIX_VALUE + k, e);
        }
        return null;
    }

    /**
     * set哈希表
     *
     * @param key
     * @param map
     * @param expired
     * @param timeUnit
     * @param timeOut
     */
    public void hmset(String key, Map<String, String> map, Boolean expired, TimeUnit timeUnit, Long timeOut) {
        stringRedisTemplate.opsForHash().putAll(initKey(key), map);
        if (expired) {
            stringRedisTemplate.expire(key, timeOut, timeUnit);
        }
    }

    /**
     * 返回某哈希表中，指定键列表
     *
     * @param key
     * @param hashKeys
     * @param <T>
     * @return
     */
    public <T> List<T> hmget(String key, Collection<String> hashKeys, Class<T> clazz) {
        HashOperations<String, String, String> stringObjectObjectHashOperations = stringRedisTemplate.opsForHash();
        List<String> objects = stringObjectObjectHashOperations.multiGet(initKey(key), hashKeys);
        List<T> resultList = new ArrayList<>();
        for (String result : objects) {
            if (Objects.nonNull(result)) {
                resultList.add(JSON.parseObject(result, clazz));
            }
        }
        return resultList;
    }

    /**
     * 返回某哈希中所有key
     *
     * @param key
     * @return
     */
    public List<Object> hkeys(String key) {
        return stringRedisTemplate.opsForHash().keys(initKey(key)).stream().collect(Collectors.toList());
    }

    private String initKey(String key) {
        return KEY_PREFIX_VALUE + key;
    }
}
package com.hxl.redis.load;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author hxl
 * @description
 * @Date 2023-06-14 16:19
 **/
public interface RedisService {

    /**
     * *公共基础操作*
     **/
    Boolean exists(String key);

    Boolean expire(String key, long timeout);

    Boolean expire(final String key, final long timeout, final TimeUnit unit);

    int delete(String... keys);

    Boolean delete(String key);

    /**
     * *基础类型(Integer、String、实体类等) 操作*
     **/
    Boolean set(String key, Object value);

    Boolean set(String key, Object value, long expireTime);

    Boolean set(String key, Object value, long expireTime, TimeUnit timeUnit);

    String get(String key);

    <T> T getObject(String key, Class<T> type);

    <T> List<T> getObjectList(String key, Class<T> type);

    /**
     * *双向列表 list操作*
     **/
    Long setCacheList(String key, Object value);

    Long setCacheList(String key, List<Object> values);

    Long setLeftCacheList(String key, Object value);

    Long setLeftCacheList(String key, List<Object> values);

    <T> T rightPop(String key, Class<T> type);

    <T> T leftPop(String key, Class<T> type);

    <T> List<T> getCacheList(String key, Class<T> type);

    Long size(String key);

    /**
     * *hash map操作*
     */
    Boolean hPut(String key, String hashKey, Object value);

    Boolean hPutAll(String key, Map<String, String> maps);

    Boolean hExists(String key, String field);

    Long hDelete(String key, Object... fields);

    Map<Object, Object> hGetAll(String key);

    Object hGet(String key, String field);

    List<Object> hMultiGet(String key, Collection<Object> fields);

    Long hSize(String key);

    /**
     * *set 集合 不重复*
     */
    boolean addSet(String key, Object value);

    boolean removeSet(String key, Object... value);

    Set<Object> getAllSet(String key);

    /**
     * *set 有序集合 不重复*
     */
    Boolean addZSet(String key, Object value, double score);

    Long removeZSet(String key, Object... value);

    /**
     * ZSet数据增加分数
     *
     * @param key   key
     * @param value value
     * @param delta 分数
     * @return duuble
     */
    Double incrZSet(String key, Object value, double delta);

    /**
     * 升序 区间查询
     *
     * @param key key
     * @param min 最小分数
     * @param max 最大分数
     * @return Set<Object>
     */
    Set<Object> zSetRangeByScore(String key, double min, double max);

    /**
     * 降序 区间查询
     *
     * @param key key
     * @param min 最小分数
     * @param max 最大分数
     * @return Set<Object>
     */
    Set<Object> zSetRangeDescByScore(String key, double min, double max);
}

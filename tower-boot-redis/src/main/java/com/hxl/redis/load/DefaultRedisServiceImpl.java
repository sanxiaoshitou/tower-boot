package com.hxl.redis.load;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author hxl
 * @description
 * @Date 2023-06-14 16:19
 **/
@Slf4j
public class DefaultRedisServiceImpl implements RedisService {

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public DefaultRedisServiceImpl(ObjectMapper objectMapper, RedisTemplate<String, Object> redisTemplate) {
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Boolean expire(String key, long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    @Override
    public int delete(String... keys) {
        int i = 0;
        for (String key : keys) {
            Boolean success = redisTemplate.delete(key);
            if (success) {
                i++;
            }
        }
        log.info("删除key,影响条数:{}", i);
        return i;
    }

    @Override
    public Boolean delete(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean set(String key, Object value) {
        if (!StringUtils.isEmpty(key)) {
            try {
                redisTemplate.opsForValue().set(key, value);
                return true;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;
    }

    @Override
    public Boolean set(String key, Object value, long expireTime) {
        return set(key, value, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public Boolean set(String key, Object value, long expireTime, TimeUnit timeUnit) {
        if (!StringUtils.isEmpty(key)) {
            try {
                redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
                return true;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return false;
    }

    @Override
    public String get(String key) {
        Object result;
        try {
            result = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
        return result == null ? null : String.valueOf(result);
    }

    @Override
    public <T> T getObject(String key, Class<T> type) {
        Object result;
        result = redisTemplate.opsForValue().get(key);
        if (result == null) {
            return null;
        }
        return transitionClass(result, type);
    }

    @Override
    public <T> List<T> getObjectList(String key, Class<T> type) {
        Object result = redisTemplate.opsForValue().get(key);
        if (result == null) {
            return null;
        }
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, type);
            return this.objectMapper.readValue(this.objectMapper.writeValueAsBytes(result), javaType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Long setCacheList(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    @Override
    public Long setCacheList(String key, List<Object> values) {
        if (StrUtil.isBlank(key) || CollectionUtil.isEmpty(values)) {
            return 0L;
        }
        try {
            return redisTemplate.opsForList().rightPushAll(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }

    @Override
    public Long setLeftCacheList(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    @Override
    public Long setLeftCacheList(String key, List<Object> values) {
        if (StrUtil.isBlank(key) || CollectionUtil.isEmpty(values)) {
            return 0L;
        }
        try {
            return redisTemplate.opsForList().leftPushAll(key, values);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }

    @Override
    public <T> T rightPop(String key, Class<T> type) {
        Object result = redisTemplate.opsForList().rightPop(key);
        if (result == null) {
            return null;
        }
        return transitionClass(result, type);
    }

    @Override
    public <T> T leftPop(String key, Class<T> type) {
        Object result = redisTemplate.opsForList().leftPop(key);
        if (result == null) {
            return null;
        }
        return transitionClass(result, type);
    }

    @Override
    public <T> List<T> getCacheList(String key, Class<T> type) {
        List<Object> resultList = redisTemplate.opsForList().range(key, 0, -1);
        if (CollectionUtil.isNotEmpty(resultList)) {
            return resultList.stream().map(i -> transitionClass(i, type)).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Long size(String key) {
        return redisTemplate.opsForList().size(key);
    }

    @Override
    public Boolean hPut(String key, String hashKey, Object value) {
        if (StrUtil.isBlank(key) || StrUtil.isBlank(hashKey)) {
            return false;
        }
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Boolean hPutAll(String key, Map<String, String> maps) {
        if (StrUtil.isBlank(key) || CollectionUtil.isEmpty(maps)) {
            return false;
        }
        try {
            redisTemplate.opsForHash().putAll(key, maps);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Boolean hExists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    @Override
    public Long hDelete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    @Override
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public List<Object> hMultiGet(String key, Collection<Object> fields) {
        return redisTemplate.opsForHash().multiGet(key, fields);
    }

    @Override
    public Long hSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    @Override
    public boolean addSet(String key, Object value) {
        if (Objects.isNull(value)) {
            return false;
        }
        try {
            Long ret = redisTemplate.opsForSet().add(key, value);
            return ret > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean removeSet(String key, Object... value) {
        if (Objects.isNull(value)) {
            return false;
        }
        try {
            Long ret = redisTemplate.opsForSet().remove(key, value);
            return ret > 0;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Set<Object> getAllSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    @Override
    public Boolean addZSet(String key, Object value, double score) {
        if (Objects.isNull(value)) {
            return false;
        }
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Long removeZSet(String key, Object... value) {
        return redisTemplate.opsForZSet().remove(key,value);
    }

    @Override
    public Double incrZSet(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    @Override
    public Set<Object> zSetRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    @Override
    public Set<Object> zSetRangeDescByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().reverseRangeByScore(key, min, max);
    }

    /**
     * 公共转换
     *
     * @param <T>
     * @return
     */
    private <T> T transitionClass(Object result, Class<T> type) {
        String expectClass = type.toString();
        String actualClass = result.getClass().toString();
        if (!expectClass.equals(actualClass)) {
            log.error("class not match.expect class:" + expectClass + ",actualClass:" + actualClass);
            return null;
        }
        try {
            return this.objectMapper.readValue(this.objectMapper.writeValueAsBytes(result), type);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
            return null;
        }
    }
}

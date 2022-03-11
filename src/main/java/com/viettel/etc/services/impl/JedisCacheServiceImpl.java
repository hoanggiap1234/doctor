package com.viettel.etc.services.impl;

import com.viettel.etc.services.JedisCacheService;
import com.viettel.etc.utils.FnCommon;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@Service
public class JedisCacheServiceImpl implements JedisCacheService {
    private static final Logger LOGGER = Logger.getLogger(FnCommon.class);

    @Qualifier("redisTemplate")
    @Autowired
    RedisTemplate redisTemplateCluster;

    @Override
    public void hset(String key, String field, String value) {
        redisTemplateCluster.opsForHash().put(key, field, value);
    }

    @Override
    public void hsetExpire(String key, String field, String value, Integer expire) throws ParseException {
        redisTemplateCluster.opsForHash().put(key, field, value);
        redisTemplateCluster.expire(key, expire, TimeUnit.MINUTES);
    }

    @Override
    public Object hget(String key, String field) {
        return redisTemplateCluster.opsForHash().get(key, field);
    }


    @Override
    public Object hdelete(String key, String field) {
        return redisTemplateCluster.opsForHash().delete(key, field);
    }

    @Override
    public Long getExpire(String key) {
        return redisTemplateCluster.getExpire(key);
    }
}

package com.viettel.etc.services;

import java.text.ParseException;

public interface JedisCacheService {
    void hset(String key, String field, String value);

    void hsetExpire(String key, String field, String value, Integer expire) throws ParseException;

    Object hget(String key, String field);

    Object hdelete(String key, String field);

    Long getExpire(String key);
}

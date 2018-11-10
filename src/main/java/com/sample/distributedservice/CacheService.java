package com.sample.distributedservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CacheService {

    CacheManager cacheManager;

    @Autowired
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public Integer getInteger(int value){
        return Optional.ofNullable((Integer) cacheManager.getCache("integerCache").get(value).get()).orElse(null);
    }

    public int putInteger(int value){
        cacheManager.getCache("integerCache").put(value,value);
        return value;
    }
}
package com.jpnie.demo.annotation;

import org.springframework.stereotype.Service;

/**
 * Created by njp on 18/5/3.
 */
@Service
public class DemoService {
    @CacheRedis(key = "test1",expireTime = 100)
    public int test1(int i){
        return i;
    }

    @CacheRedis(key="test2")
    public String test2(String str){
        return str;
    }
}

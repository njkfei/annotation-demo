package com.jpnie.demo.annotation;

import java.lang.annotation.*;

/**
 * Created by njp on 18/5/3.
 */
@Target(ElementType.METHOD)
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheRedis {
    String key();
    int expireTime() default 60000;
}

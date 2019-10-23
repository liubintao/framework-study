package com.robust.study.netty.lion.cache.redis;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/9/11 13:01
 * @Version: 1.0
 */
public class RedisException extends RuntimeException {

    public RedisException(String message) {
        super(message);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
    }

    public RedisException(Throwable cause) {
        super(cause);
    }
}


package com.robust.study.netty.lion.test.redis;

import com.robust.study.netty.lion.cache.redis.manager.RedisManager;
import com.robust.study.netty.lion.cache.redis.mq.ListenerDispatcher;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

public class PubSubTest {
    ListenerDispatcher listenerDispatcher = new ListenerDispatcher();

    @Before
    public void init() {
    }

    @Test
    public void subpubTest() {

        RedisManager.I.subscribe(listenerDispatcher.getSubscriber(), "/hello/123");
        RedisManager.I.subscribe(listenerDispatcher.getSubscriber(), "/hello/124");
        RedisManager.I.publish("/hello/123", "123");
        RedisManager.I.publish("/hello/124", "124");
    }

    @Test
    public void pubsubTest() {
        RedisManager.I.publish("/hello/123", "123");
        RedisManager.I.publish("/hello/124", "124");
        RedisManager.I.subscribe(listenerDispatcher.getSubscriber(), "/hello/123");
        RedisManager.I.subscribe(listenerDispatcher.getSubscriber(), "/hello/124");
    }

    @Test
    public void pubTest() {
        RedisManager.I.publish("/hello/123", "123");
        RedisManager.I.publish("/hello/124", "124");
    }

    @Test
    public void subTest() {
        RedisManager.I.subscribe(listenerDispatcher.getSubscriber(), "/hello/123");
        RedisManager.I.subscribe(listenerDispatcher.getSubscriber(), "/hello/124");
        LockSupport.park();
    }

}

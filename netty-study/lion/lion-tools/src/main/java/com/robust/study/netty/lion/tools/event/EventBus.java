
package com.robust.study.netty.lion.tools.event;

import com.google.common.eventbus.AsyncEventBus;
import com.robust.study.netty.lion.api.event.Event;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;

/**
 */
@Slf4j
public class EventBus {
    private static com.google.common.eventbus.EventBus eventBus;

    public static void create(Executor executor) {
        eventBus = new AsyncEventBus(executor, (exception, context)
                -> log.error("event bus subscriber ex", exception));
    }

    public static void post(Event event) {
        eventBus.post(event);
    }

    public static void register(Object bean) {
        eventBus.register(bean);
    }

    public static void unregister(Object bean) {
        eventBus.unregister(bean);
    }

}

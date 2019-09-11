
package com.robust.study.netty.lion.tools.event;

public abstract class EventConsumer {

    public EventConsumer() {
        EventBus.register(this);
    }

}

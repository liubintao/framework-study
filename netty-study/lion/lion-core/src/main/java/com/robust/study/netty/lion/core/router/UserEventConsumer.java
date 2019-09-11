
package com.robust.study.netty.lion.core.router;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.robust.study.netty.lion.api.event.Topics;
import com.robust.study.netty.lion.api.event.UserOfflineEvent;
import com.robust.study.netty.lion.api.event.UserOnlineEvent;
import com.robust.study.netty.lion.api.spi.common.MQClient;
import com.robust.study.netty.lion.api.spi.common.MQClientFactory;
import com.robust.study.netty.lion.common.router.RemoteRouterManager;
import com.robust.study.netty.lion.common.user.UserManager;
import com.robust.study.netty.lion.tools.event.EventConsumer;


public final class UserEventConsumer extends EventConsumer {

    private final MQClient mqClient = MQClientFactory.create();

    private final UserManager userManager;

    public UserEventConsumer(RemoteRouterManager remoteRouterManager) {
        this.userManager = new UserManager(remoteRouterManager);
    }

    @Subscribe
    @AllowConcurrentEvents
    void on(UserOnlineEvent event) {
        userManager.addToOnlineList(event.getUserId());
        mqClient.publish(Topics.ONLINE_CHANNEL, event.getUserId());
    }

    @Subscribe
    @AllowConcurrentEvents
    void on(UserOfflineEvent event) {
        userManager.remFormOnlineList(event.getUserId());
        mqClient.publish(Topics.OFFLINE_CHANNEL, event.getUserId());
    }

    public UserManager getUserManager() {
        return userManager;
    }
}

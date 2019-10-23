
package com.robust.study.netty.lion.common.router;


import com.robust.study.netty.lion.api.router.ClientLocation;
import com.robust.study.netty.lion.api.router.Router;

/**
 */
public final class RemoteRouter implements Router<ClientLocation> {
    private final ClientLocation clientLocation;

    public RemoteRouter(ClientLocation clientLocation) {
        this.clientLocation = clientLocation;
    }

    public boolean isOnline() {
        return clientLocation.isOnline();
    }

    public boolean isOffline() {
        return clientLocation.isOffline();
    }

    @Override
    public ClientLocation getRouteValue() {
        return clientLocation;
    }

    @Override
    public Router.RouterType getRouteType() {
        return RouterType.REMOTE;
    }

    @Override
    public String toString() {
        return "RemoteRouter{" + clientLocation + '}';
    }
}

package com.robust.study.netty.lion.common;

import com.robust.study.netty.lion.api.srd.CommonServiceNode;
import com.robust.study.netty.lion.api.srd.ServiceNames;
import com.robust.study.netty.lion.api.srd.ServiceNode;
import com.robust.study.netty.lion.tools.config.CC;
import com.robust.study.netty.lion.tools.config.ConfigTools;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/28 11:59
 * @Version: 1.0
 */
public class ServerNodes {

    public static ServiceNode cs() {
        CommonServiceNode node = new CommonServiceNode();
        node.setHost(ConfigTools.getConnectServerRegisterIp());
        node.setPort(CC.lion.net.connect_server_port);
        node.setPersistent(false);
        node.setServiceName(ServiceNames.CONN_SERVER);
        node.setAttrs(CC.lion.net.connect_server_register_attr);
        return node;
    }

    public static ServiceNode ws() {
        CommonServiceNode node = new CommonServiceNode();
        node.setHost(ConfigTools.getConnectServerRegisterIp());
        node.setPort(CC.lion.net.ws_server_port);
        node.setPersistent(false);
        node.setServiceName(ServiceNames.WS_SERVER);
        return node;
    }

    public static ServiceNode gs() {
        CommonServiceNode node = new CommonServiceNode();
        node.setHost(ConfigTools.getGatewayServerRegisterIp());
        node.setPort(CC.lion.net.gateway_server_port);
        node.setPersistent(false);
        node.setServiceName(ServiceNames.GATEWAY_SERVER);
        return node;
    }
}

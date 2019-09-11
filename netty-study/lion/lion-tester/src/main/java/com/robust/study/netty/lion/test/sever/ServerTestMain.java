
package com.robust.study.netty.lion.test.sever;

import com.robust.study.netty.lion.boot.Main;
import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 */
public class ServerTestMain {

    public static void main(String[] args) {
        start();
    }

    @Test
    public void testServer() {
        start();
        LockSupport.park();
    }

    public static void start() {
        System.setProperty("io.netty.leakDetection.level", "PARANOID");
        System.setProperty("io.netty.noKeySetOptimization", "false");
        Main.main(null);
    }

}

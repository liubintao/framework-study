
package com.robust.study.netty.lion.test.util;

import com.robust.study.netty.lion.tools.Utils;
import org.junit.Test;

/**
 */
public class IPTest {
    @Test
    public void getLocalIP() throws Exception {
        System.out.println(Utils.lookupLocalIp());
        System.out.println(Utils.lookupExtranetIp());

    }
}



package com.robust.study.netty.lion.core.ack;


public interface AckCallback {
    void onSuccess(AckTask context);

    void onTimeout(AckTask context);
}

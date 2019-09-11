package com.robust.study.netty.lion.api.connection;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/27 17:54
 * @Version: 1.0
 */
public interface Cipher {

    byte[] encrypt(byte[] data);

    byte[] decrypt(byte[] data);
}

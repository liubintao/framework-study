
package com.robust.study.netty.lion.common.security;


import com.robust.study.netty.lion.api.connection.Cipher;
import com.robust.study.netty.lion.api.spi.SPI;
import com.robust.study.netty.lion.api.spi.core.RsaCipherFactory;

/**
 */
@SPI
public class DefaultRsaCipherFactory implements RsaCipherFactory {
    private static final RsaCipher RSA_CIPHER = RsaCipher.create();

    @Override
    public Cipher get() {
        return RSA_CIPHER;
    }
}

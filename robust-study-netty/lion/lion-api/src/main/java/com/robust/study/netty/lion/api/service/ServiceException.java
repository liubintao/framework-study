package com.robust.study.netty.lion.api.service;

/**
 * @Description:
 * @Author: robust
 * @CreateDate: 2019/8/29 16:36
 * @Version: 1.0
 */
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

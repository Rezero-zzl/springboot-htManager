package com.zzl.springboot.exception;

import lombok.Getter;

/**
 * 自定义异常
 * @author TickNet-zzl
 * @date 2022/4/12  19:37
 */
@Getter
public class ServiceException extends RuntimeException{
    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}

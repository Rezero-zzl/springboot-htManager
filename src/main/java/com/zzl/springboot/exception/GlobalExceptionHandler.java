package com.zzl.springboot.exception;

import com.zzl.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * springboot  自定义全局异常类
 * @author TickNet-zzl
 * @date 2022/4/12  19:23
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 抛出ServiceException时，调用
     * @param se
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se){
        //TODO 日志记录模块
        return Result.error(se.getCode(),se.getMessage());
    }
}


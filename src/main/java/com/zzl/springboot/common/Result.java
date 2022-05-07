package com.zzl.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回结果包装类
 * @author TickNet-zzl
 * @date 2022/4/12  18:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success(){
        return new Result(Constants.CODE_200,"操作成功",null);
    }

    public static Result success(Object data){
        return new Result(Constants.CODE_200,"操作成功",data);
    }

    public static Result error(String code,String msg){
        return new Result(code,msg,null);
    }

    public static Result error(){
        return new Result(Constants.CODE_500,"系统错误",null);
    }
}

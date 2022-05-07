package com.zzl.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author TickNet-zzl
 * @date 2022/4/18  20:32
 */
@Data
@TableName("sys_dict")
public class Dict {

    private String name;
    private String value;
    private String type;

}

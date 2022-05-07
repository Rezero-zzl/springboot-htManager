package com.zzl.springboot.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author TickNet-zzl
 * @date 2022/4/9  15:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    /** 用户名 */
    @Alias(value = "用户名")
    @TableField(value = "username")
    private String userName;
    /** 密码(密码不向前端展示) */
    @JsonIgnore
    @Alias(value = "密码")
    @TableField(value = "password")
    private String password;
    /** 昵称 */
    @Alias(value = "昵称")
    @TableField(value = "nickname")
    private String nickName;
    /** 邮箱 */
    @Alias(value = "邮箱")
    @TableField(value = "email")
    private String email;
    /** 电话 */
    @Alias(value = "电话")
    @TableField(value = "phone")
    private String phone;
    /** 地址 */
    @Alias(value = "地址")
    @TableField(value = "address")
    private String address;
    /** 创建时间 */
    @Alias(value = "注册时间")
    @TableField(value = "create_time")
    private Date createTime;
    /** 头像 */
    @Alias(value = "用户头像")
    @TableField(value = "avatarUrl")
    private String avatarUrl;

    @Alias(value = "角色")
    @TableField(value = "role")
    private String role;
}

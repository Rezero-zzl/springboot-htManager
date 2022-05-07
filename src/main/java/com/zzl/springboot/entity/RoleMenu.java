package com.zzl.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色-菜单实体
 * @author TickNet-zzl
 * @date 2022/4/18  21:34
 */
@Data
@TableName("sys_role_menu")
public class RoleMenu {

    private Integer roleId;
    private Integer menuId;

}

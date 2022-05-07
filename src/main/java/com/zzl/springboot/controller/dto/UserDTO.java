package com.zzl.springboot.controller.dto;

import com.zzl.springboot.entity.Menu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 接收前端登录请求参数
 * @author TickNet-zzl
 * @date 2022/4/12  15:39
 */
@Data
public class UserDTO {
    private String userName;
    private String password;
    private String nickName;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}

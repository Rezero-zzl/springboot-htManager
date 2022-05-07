package com.zzl.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzl.springboot.controller.dto.UserDTO;
import com.zzl.springboot.entity.User;

/**
 * 系统操作接口
 * @author TickNet-zzl
 * @date 2022/4/14  15:47
 */
public interface ISysService extends IService<User> {

    UserDTO login(UserDTO userDTO);

    User register(UserDTO userDTO);
}

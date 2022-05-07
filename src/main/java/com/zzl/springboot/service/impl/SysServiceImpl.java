package com.zzl.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzl.springboot.common.Constants;
import com.zzl.springboot.common.Result;
import com.zzl.springboot.controller.dto.UserDTO;
import com.zzl.springboot.entity.Menu;
import com.zzl.springboot.entity.User;
import com.zzl.springboot.exception.ServiceException;
import com.zzl.springboot.mapper.RoleMapper;
import com.zzl.springboot.mapper.RoleMenuMapper;
import com.zzl.springboot.mapper.UserMapper;
import com.zzl.springboot.service.IMenuService;
import com.zzl.springboot.service.ISysService;
import com.zzl.springboot.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统操作实现
 * @author TickNet-zzl
 * @date 2022/4/12  15:42
 */
@Service
public class SysServiceImpl extends ServiceImpl<UserMapper,User> implements ISysService {

     @Resource
     private RoleMapper roleMapper;
     @Resource
     private RoleMenuMapper roleMenuMapper;
     @Resource
     private IMenuService menuService;

    /**
     * 通过用户名密码校验登录
     * @param userDTO
     * @return
     */
    @Override
    public UserDTO login(UserDTO userDTO) {
        // 入参校验
        String userName = userDTO.getUserName();
        String password = userDTO.getPassword();
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)){
            throw new ServiceException(Constants.CODE_400,"参数错误");
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userName).eq("password",password);
        User user;
        try{
            user = getOne(queryWrapper);
        }catch(Exception e){
            //TODO 如果出现数据库根据登录条件查出超过一条数据，则必是出现漏洞（数据库设置用户名，密码字段唯一），记录日志
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        if (user != null){
            BeanUtils.copyProperties(user,userDTO);
//            使用Hutool的由于设置了@Alias，直接复制有问题，要改userDTO
//            BeanUtil.copyProperties(user,userDTO,true);
            String token = TokenUtils.genToken(user.getId().toString(),user.getPassword());
            userDTO.setToken(token);
            String role = user.getRole();
            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);
            return userDTO;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        // 入参校验，判断用户是否已经注册
        String userName = userDTO.getUserName();
        if (StrUtil.isBlank(userName)){
            throw new ServiceException(Constants.CODE_400,"参数错误");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",userName);
        User user;
        try{
            user = getOne(queryWrapper);
        }catch (Exception e){
            //TODO 日志记录(不应出现账户相同的两个数据行)
            throw new ServiceException(Constants.CODE_500,"系统错误");
        }
        if (user != null){
            throw new ServiceException(Constants.CODE_600,"用户已注册");
        }else{
            user = new User();
            BeanUtils.copyProperties(userDTO,user);
            //TODO 用户随机昵称生成
            save(user);
            return user;
        }
    }

    /**
     * 获取房前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag){
        // 当前角色的所有菜单id集合
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        // 查出当前所有菜单，在筛选当前用户角色菜单
        List<Menu> menus = menuService.queryAll("");
        List<Menu> roleMenus = new ArrayList<>();
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())){
                roleMenus.add(menu);
            }
            // 所有菜单中移除不在当前角色菜单的选项
            List<Menu> children = menu.getChildren();
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}

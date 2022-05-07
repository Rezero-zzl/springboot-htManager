package com.zzl.springboot.controller;


import com.zzl.springboot.common.Constants;
import com.zzl.springboot.common.Result;
import com.zzl.springboot.entity.Role;
import com.zzl.springboot.exception.ServiceException;
import com.zzl.springboot.mapper.RoleMenuMapper;
import com.zzl.springboot.service.IRoleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author generator
 * @since 2022-04-17
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private IRoleService roleService;


    /**
     * 新增或修改
     * @param role
     * @return
     */
    @PostMapping("/add")
    public Result insertOrUpdate(@RequestBody Role role){
        boolean isSuccess = roleService.insertOrUpdate(role);
        if (isSuccess){
            return Result.success();
        }else {
            throw new ServiceException(Constants.CODE_600,"操作失败，请联系管理员!");
        }
    }

    /**
     * 查询
     * @return
     */
    @GetMapping("/query")
    public Result queryAll(){
        return Result.success(roleService.queryAll());
    }

    /**
     *  个人信息展示（获取前端localstorage里存储的userName信息作为参数）
     * @param name
     * @return
     */
    @GetMapping("/username/{userName}")
    public Result queryByUserName(@PathVariable String name){
        return Result.success(roleService.queryByUserName(name));
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        boolean isSuccess = roleService.deleteById(id);
        if (isSuccess){
            return Result.success();
        }else {
            throw new ServiceException(Constants.CODE_600,"操作失败，请联系管理员!");
        }
    }

    @PostMapping("/deleteBatch")
    public Result deleteByIds(@RequestBody List<Integer> ids){
        boolean isSuccess = roleService.deleteByIds(ids);
        if (isSuccess){
            return Result.success();
        }else {
            throw new ServiceException(Constants.CODE_600,"操作失败，请联系管理员!");
        }
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result queryPage(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "2") Integer pageSize,
                            @RequestParam(defaultValue = "") String name){
        return Result.success(roleService.queryPage(pageNum,pageSize,name));
    }

    /**
     * 文件从浏览器导出
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        roleService.export(response);
    }

    /**
     * 文件导入
     * @param file
     */
    @PostMapping("/import")
    public void DoImport(MultipartFile file) throws IOException {
        roleService.DoImport(file);
    }

    /**
     * 绑定角色菜单关系
     * @param roleId
     * @param menuIds
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer> menuIds){
        roleService.setRoleMenu(roleId,menuIds);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId){
        return Result.success(roleService.getRoleMenu(roleId));
    }
}


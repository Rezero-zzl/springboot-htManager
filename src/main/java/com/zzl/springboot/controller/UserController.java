package com.zzl.springboot.controller;

import com.zzl.springboot.common.Constants;
import com.zzl.springboot.common.Result;
import com.zzl.springboot.entity.User;
import com.zzl.springboot.exception.ServiceException;
import com.zzl.springboot.service.IUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 用户操作控制器
 * @author TickNet-zzl
 * @date 2022/4/9  16:03
 * controller 用于与前端交互
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    /**
     * 新增或修改
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result insertOrUpdate(@RequestBody User user){
        boolean isSuccess = userService.insertOrUpdate(user);
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
        return Result.success(userService.queryAll());
    }

    /**
     *  个人信息展示（获取前端localstorage里存储的userName信息作为参数）
     * @param userName
     * @return
     */
    @GetMapping("/username/{userName}")
    public Result queryByUserName(@PathVariable String userName){
        return Result.success(userService.queryByUserName(userName));
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        boolean isSuccess = userService.deleteById(id);
        if (isSuccess){
            return Result.success();
        }else {
            throw new ServiceException(Constants.CODE_600,"操作失败，请联系管理员!");
        }
    }

    @PostMapping("/deleteBatch")
    public Result deleteByIds(@RequestBody List<Integer> ids){
        boolean isSuccess = userService.deleteByIds(ids);
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
     * @param userName
     * @param email
     * @param address
     * @return
     */
    @GetMapping("/page")
    public Result queryPage(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "2") Integer pageSize,
                           @RequestParam(defaultValue = "") String userName, @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address){
        return Result.success(userService.queryPage(pageNum,pageSize,userName,email,address));
    }

    /**
     * 文件从浏览器导出
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        userService.export(response);
    }

    /**
     * 文件导入
     * @param file
     */
    @PostMapping("/import")
    public void DoImport(MultipartFile file) throws IOException {
        userService.DoImport(file);
    }
}

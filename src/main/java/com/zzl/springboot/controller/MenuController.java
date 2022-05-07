package com.zzl.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzl.springboot.common.Constants;
import com.zzl.springboot.common.Result;
import com.zzl.springboot.entity.Dict;
import com.zzl.springboot.entity.Menu;
import com.zzl.springboot.exception.ServiceException;
import com.zzl.springboot.mapper.DictMapper;
import com.zzl.springboot.service.IMenuService;
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
@RequestMapping("/menu")
public class MenuController {
    @Resource
    private IMenuService menuService;
    @Resource
    private DictMapper dictMapper;

    @GetMapping("/ids")
    public Result findAllIds(){
        return Result.success(menuService.list().stream().map(Menu::getId));
    }

    /**
     * 新增或修改
     * @param menu
     * @return
     */
    @PostMapping("/add")
    public Result insertOrUpdate(@RequestBody Menu menu){
        Boolean isSuccess = menuService.insertOrUpdate(menu);
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
    public Result queryAll(@RequestParam(defaultValue = "") String name){
        return Result.success(menuService.queryAll(name));
    }

    /**
     *  个人信息展示（获取前端localstorage里存储的userName信息作为参数）
     * @param name
     * @return
     */
    @GetMapping("/username/{userName}")
    public Result queryByUserName(@PathVariable String name){
        return Result.success(menuService.queryByUserName(name));
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        Boolean isSuccess = menuService.deleteById(id);
        if (isSuccess){
            return Result.success();
        }else {
            throw new ServiceException(Constants.CODE_600,"操作失败，请联系管理员!");
        }
    }

    @PostMapping("/deleteBatch")
    public Result deleteByIds(@RequestBody List<Integer> ids){
        Boolean isSuccess = menuService.deleteByIds(ids);
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
        return Result.success(menuService.queryPage(pageNum,pageSize,name));
    }

    /**
     * 文件从浏览器导出
     * @param response
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        menuService.export(response);
    }

    /**
     * 文件导入
     * @param file
     */
    @PostMapping("/import")
    public void DoImport(MultipartFile file) throws IOException {
        menuService.DoImport(file);
    }

    @GetMapping("/icons")
    public Result getIcons(){
        QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(null));
    }
}


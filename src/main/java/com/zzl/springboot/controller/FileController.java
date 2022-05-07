package com.zzl.springboot.controller;

import com.zzl.springboot.common.Result;
import com.zzl.springboot.entity.MyFile;
import com.zzl.springboot.service.IFileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文件上传下载控制器
 * @author TickNet-zzl
 * @date 2022/4/14  15:15
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private IFileService fileService;

    /**
     * 文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        return fileService.upload(file);
    }

    /**
     * 文件下载
     * @param fileUUid
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/{fileUUid}")
    public void download(@PathVariable String fileUUid, HttpServletResponse response) throws IOException {
        fileService.download(fileUUid,response);
    }

    @PostMapping("/update")
    public Result updateDownloadState(@RequestBody MyFile file){
        return fileService.updateDownloadState(file) ? Result.success() : Result.error();
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
        return Result.success(fileService.queryPage(pageNum,pageSize,name));
    }

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){
        return fileService.deleteById(id) ? Result.success() : Result.error();
    }

    /**
     * 批量逻辑删除
     * @param ids
     * @return
     */
    @PostMapping("/deleteBatch")
    public Result deleteByIds(@RequestBody List<Integer> ids){
        return fileService.deleteByIds(ids) ? Result.success() : Result.error();
    }

}

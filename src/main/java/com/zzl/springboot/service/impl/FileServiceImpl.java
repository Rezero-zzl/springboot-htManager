package com.zzl.springboot.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zzl.springboot.common.Constants;
import com.zzl.springboot.common.Result;
import com.zzl.springboot.entity.MyFile;
import com.zzl.springboot.entity.User;
import com.zzl.springboot.exception.ServiceException;
import com.zzl.springboot.mapper.FileMapper;
import com.zzl.springboot.service.IFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件上传、下载服务实现
 * @author TickNet-zzl
 * @date 2022/4/14  15:36
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper,MyFile> implements IFileService {

    @Value("${files.upload.path}")
    private String fileUploadPath;
    @Value("${server.ip}")
    private String serverIp;

    @Override
    public String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        // 定义根路径
        File uploadParentFile = new File(fileUploadPath);
        // 判断文件目录是否已存在
        if (!uploadParentFile.exists()){
            uploadParentFile.mkdirs();
        }
        //  定义一个文件唯一的标识码,避免重名
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUid = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileUUid);
        // 获取文件的md5，用于避免存储重复图片
        String md5 = SecureUtil.md5(file.getInputStream());
        MyFile myFile = getFileByMd5(md5);
        String url;
        if (myFile != null) {
            url = myFile.getUrl();
        }else {
            // 将获取到的文件存到指定路径
            file.transferTo(uploadFile);
            url = "http://" + serverIp + ":9090/file/download/" + fileUUid;
        }

        // 存入数据库
        MyFile saveFile = new MyFile();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        save(saveFile);
        return url;
    }

    @Override
    public void download(String fileUUid,HttpServletResponse response) throws IOException {
        //TODO 当下载链接已禁用

        // 根据文件唯一标识码获取文件
        File downloadFile = new File(fileUploadPath + fileUUid);
        // 设置输出流格式
        ServletOutputStream outputStream = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileUUid,"UTF-8"));
        response.setContentType("application/octet-stream");
        // 读取文件的字节流
        outputStream.write(FileUtil.readBytes(downloadFile));
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public IPage<MyFile> queryPage(Integer pageNum, Integer pageSize, String name) {
        IPage<MyFile> page = new Page<>(pageNum,pageSize);
        QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name).eq("is_delete",false).orderByDesc("id");
        return page(page,queryWrapper);
    }

    @Override
    public Boolean deleteById(Integer id) {
        //TODO 可以精简，此处需要查询两次数据库
        MyFile myFile = getById(id);
        myFile.setIsDelete(true);
        return updateById(myFile);
    }

    @Override
    public Boolean deleteByIds(List<Integer> ids) {
        List<MyFile> myFiles = listByIds(ids);
        for(MyFile file : myFiles) {
            file.setIsDelete(true);
        }
        return updateBatchById(myFiles);
    }

    @Override
    public Boolean updateDownloadState(MyFile file) {
        return updateById(file);
    }

    private MyFile getFileByMd5(String md5) {
        //根据md5，查数据库，看磁盘是否已存在相应图片
        QueryWrapper<MyFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        List<MyFile> list = list(queryWrapper);
        return list.size() == 0 ? null : list.get(0);
    }
}

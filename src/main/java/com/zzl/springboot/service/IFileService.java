package com.zzl.springboot.service;

import cn.hutool.http.server.HttpServerResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzl.springboot.common.Result;
import com.zzl.springboot.entity.MyFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 文件操作接口
 * @author TickNet-zzl
 * @date 2022/4/14  15:35
 */
public interface IFileService extends IService<MyFile> {
    /** 文件上传 */
    String upload(MultipartFile file) throws IOException;
    /** 文件下载 */
    void download(String uuid,HttpServletResponse response) throws IOException;
    /** 查询分页 */
    IPage<MyFile> queryPage(Integer pageNum, Integer pageSize, String name);
    /** 逻辑删除 */
    Boolean deleteById(Integer id);
    /** 批量逻辑删除 */
    Boolean deleteByIds(List<Integer> ids);
    /** 更新启用状态 */
    Boolean updateDownloadState(MyFile file);
}

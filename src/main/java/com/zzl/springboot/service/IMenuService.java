package com.zzl.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zzl.springboot.common.Result;
import com.zzl.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzl.springboot.entity.Role;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author generator
 * @since 2022-04-17
 */
public interface IMenuService extends IService<Menu> {

    /** 新增或修改接口 */
    Boolean insertOrUpdate(Menu menu);
    /** 查询全部 */
    List<Menu> queryAll(String name);
    /** 删除单个 */
    Boolean deleteById(Integer id);
    /** 批量删除 */
    Boolean deleteByIds(List<Integer> ids);
    /** 分页查询 */
    IPage<Menu> queryPage(Integer pageNum, Integer pageSize, String name);
    /** 数据导出 */
    void export(HttpServletResponse response) throws IOException;
    /** 数据导入 */
    void DoImport(MultipartFile file) throws IOException;
    /** 主页信息查询（根据账号） */
    Menu queryByUserName(String name);
}

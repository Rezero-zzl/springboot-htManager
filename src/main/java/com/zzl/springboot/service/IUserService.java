package com.zzl.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zzl.springboot.common.Result;
import com.zzl.springboot.entity.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 用户操作接口
 * @author TickNet-zzl
 * @date 2022/4/14  15:50
 */
public interface IUserService extends IService<User> {
    /** 新增或修改接口 */
    Boolean insertOrUpdate(User user);
    /** 查询全部 */
    List<User> queryAll();
    /** 删除单个 */
    Boolean deleteById(Integer id);
    /** 批量删除 */
    Boolean deleteByIds(List<Integer> ids);
    /** 分页查询 */
    IPage<User> queryPage(Integer pageNum, Integer pageSize, String userName, String email, String address);
    /** 数据导出 */
    void export(HttpServletResponse response) throws IOException;
    /** 数据导入 */
    void DoImport(MultipartFile file) throws IOException;
    /** 主页信息查询（根据账号） */
    User queryByUserName(String userName);
}

package com.zzl.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zzl.springboot.common.Constants;
import com.zzl.springboot.common.Result;
import com.zzl.springboot.entity.User;
import com.zzl.springboot.exception.ServiceException;
import com.zzl.springboot.mapper.UserMapper;
import com.zzl.springboot.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 用户操作实现
 * @author TickNet-zzl
 * @date 2022/4/9  16:35
 * service 层进行数据校验处理（入参校验，返回值处理）
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    @Override
    public Boolean insertOrUpdate(User user){
        return saveOrUpdate(user);
    }

    @Override
    public List<User> queryAll() {
        return list();
    }

    @Override
    public Boolean deleteById(Integer id) {
        return removeById(id);
    }

    @Override
    public Boolean deleteByIds(List<Integer> ids) {
        return removeBatchByIds(ids);
    }

    @Override
    public IPage<User> queryPage(Integer pageNum, Integer pageSize, String userName, String email, String address) {
        IPage<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username",userName).like("email",email)
                .like("address",address).orderByDesc("id");
        return page(page,queryWrapper);
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        //TODO 进行异常判断，比如校验模板是否合法，给前端返回成功，或失败信息
        List<User> userList = list();
        //创建Hutool导出工具,在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义实体类属性导出别名,表头(已经改为在User实体类加@Alias注解声明)
//        writer.addHeaderAlias("userName","用户名");
//        writer.addHeaderAlias("password","密码");
//        writer.addHeaderAlias("nickName","昵称");
//        writer.addHeaderAlias("email","邮箱");
//        writer.addHeaderAlias("phone","电话");
//        writer.addHeaderAlias("address","地址");
//        writer.addHeaderAlias("createTime","创建时间");
//        writer.addHeaderAlias("avatarUrl","用户头像");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(userList,true);

        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();

    }

    @Override
    public void DoImport(MultipartFile file) throws IOException {
        //TODO 进行异常处理(文件是否合法，读取是否成功，插入是否成功)
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<User> userList = reader.readAll(User.class);
        saveBatch(userList);
    }

    @Override
    public User queryByUserName(String userName) {
        if (StrUtil.isBlank(userName)){
            //TODO 前端localstorage没存上登录用户信息，进行相应操作
            return null;
        }else {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username",userName);
            return getOne(queryWrapper);
        }
    }
}

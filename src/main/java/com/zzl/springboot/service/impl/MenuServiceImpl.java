package com.zzl.springboot.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzl.springboot.common.Constants;
import com.zzl.springboot.common.Result;
import com.zzl.springboot.entity.Menu;
import com.zzl.springboot.entity.Role;
import com.zzl.springboot.exception.ServiceException;
import com.zzl.springboot.mapper.MenuMapper;
import com.zzl.springboot.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2022-04-17
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public Boolean insertOrUpdate(Menu menu){
        return saveOrUpdate(menu);
    }

    @Override
    public List<Menu> queryAll(String name) {
        // todo 作自关联查询优化
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }
        List<Menu> list = list(queryWrapper);
        // 找出一级菜单
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        // 找出一级菜单的子菜单
        for (Menu menu : parentNodes) {
            menu.setChildren(list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList()));
        }
        return parentNodes;
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
    public IPage<Menu> queryPage(Integer pageNum, Integer pageSize, String name) {
        IPage<Menu> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name).orderByDesc("id");
        return page(page,queryWrapper);
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        //TODO 进行异常判断，比如校验模板是否合法，给前端返回成功，或失败信息
        List<Menu> userList = list();
        //创建Hutool导出工具,在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);

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
        List<Menu> menuList = reader.readAll(Menu.class);
        saveBatch(menuList);
    }

    @Override
    public Menu queryByUserName(String userName) {
        if (StrUtil.isBlank(userName)){
            //TODO 前端localstorage没存上登录用户信息，进行相应操作
            return null;
        }else {
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name",userName);
            Menu menu = getOne(queryWrapper);
            return menu;
        }
    }
}

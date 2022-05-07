package com.zzl.springboot.service.impl;

import cn.hutool.core.collection.CollUtil;
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
import com.zzl.springboot.entity.RoleMenu;
import com.zzl.springboot.entity.User;
import com.zzl.springboot.exception.ServiceException;
import com.zzl.springboot.mapper.RoleMapper;
import com.zzl.springboot.mapper.RoleMenuMapper;
import com.zzl.springboot.service.IMenuService;
import com.zzl.springboot.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author generator
 * @since 2022-04-17
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private IMenuService menuService;

    @Override
    public Boolean insertOrUpdate(Role role){
        return saveOrUpdate(role);
    }

    @Override
    public List<Role> queryAll() {
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
    public IPage<Role> queryPage(Integer pageNum, Integer pageSize, String name) {
        IPage<Role> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name).orderByDesc("id");
        return page(page,queryWrapper);
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        //TODO 进行异常判断，比如校验模板是否合法，给前端返回成功，或失败信息
        List<Role> userList = list();
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
        List<Role> roleList = reader.readAll(Role.class);
        saveBatch(roleList);
    }

    @Override
    public Role queryByUserName(String userName) {
        if (StrUtil.isBlank(userName)){
            //TODO 前端localstorage没存上登录用户信息，进行相应操作
            return null;
        }else {
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("name",userName);
            return getOne(queryWrapper);
        }
    }

    // 加上事务注解防止在删除和增加途中发生异常失败，导致角色权限异常
    @Transactional
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
        // 先删后增
        roleMenuMapper.deleteByRoleId(roleId);

        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
            // todo 优化，这里太傻逼了
            // 自动设置子菜单的父菜单（前端选取了子菜单，逻辑上父菜单得选上）
            Menu menu = menuService.getById(menuId);
            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())){
                // 只选了二级菜单并且没有传他的父级id过来，我们自己补上父级id
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menu.getPid());
                roleMenuMapper.insert(roleMenu);
                menuIdsCopy.add(menu.getPid());
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}

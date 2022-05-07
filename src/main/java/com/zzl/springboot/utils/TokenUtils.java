package com.zzl.springboot.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.zzl.springboot.entity.User;
import com.zzl.springboot.service.impl.SysServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * token工具类
 * @author TickNet-zzl
 * @date 2022/4/14  8:56
 */
@Component
public class TokenUtils {

    private static SysServiceImpl staticSysServiceImpl;
    @Resource
    private SysServiceImpl sysServiceimpl;

    // 该注解表示在服务器加载Servlet的时候执行，用于完成初始化，会在依赖注入完成后自动执行，执行
    // 顺序为 Constructor - @Resource/@Autowired - @PostConstruct
    @PostConstruct
    public void setSysService() {
        staticSysServiceImpl = sysServiceimpl;
    }

    /**
     * 生成token
     * @param userId
     * @param sign
     * @return
     */
    public static String genToken(String userId,String sign){
        return JWT.create().withAudience(userId) // 将 user id 保存到 token 里面, 作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(),2))//2小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
    }

    /**
     * 获取当前用户登录的信息
     * @return
     */
    public static User getCurrentUser() {
        try{
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticSysServiceImpl.getById(userId);
            }
        } catch (Exception e){
            return null;
        }
        return null;
    }
}

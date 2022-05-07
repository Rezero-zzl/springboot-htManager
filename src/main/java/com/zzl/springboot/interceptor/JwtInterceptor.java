package com.zzl.springboot.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zzl.springboot.common.Constants;
import com.zzl.springboot.entity.User;
import com.zzl.springboot.exception.ServiceException;
import com.zzl.springboot.service.impl.UserServiceImpl;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author TickNet-zzl
 * @date 2022/4/14  9:54
 */
public class JwtInterceptor implements HandlerInterceptor {
    @Resource
    private UserServiceImpl userServiceImpl;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过,如果不是请求后台方法则直接放行
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        // 执行认证
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)){
            throw new ServiceException(Constants.CODE_401,"未登录，请重新登录");
        }
        // 获取token中的userId
        String userId;
        try{
            userId = JWT.decode(token).getAudience().get(0);
        }catch (JWTDecodeException j){
            throw new ServiceException(Constants.CODE_401,"token验证失败，请重新登录");
        }

        User user = userServiceImpl.getById(userId);
        if (user == null){
            throw new ServiceException(Constants.CODE_401,"用户不存在，请重新登录");
        }
        // 用户密码加签验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException(Constants.CODE_401,"token验证失败，请重新登录");
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

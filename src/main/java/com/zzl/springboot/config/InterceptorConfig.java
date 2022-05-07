package com.zzl.springboot.config;

import com.zzl.springboot.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置类
 * @author TickNet-zzl
 * @date 2022/4/14  10:22
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getJwtInterceptor())
                .addPathPatterns("/**")    // 拦截所有请求，通过判断token是否合法 决定是否需要登录
                .excludePathPatterns("/sys/loginByUserInfo","/sys/register","/user/import","/user/export","/file/**");
    }

    @Bean
    public JwtInterceptor getJwtInterceptor(){
        return new JwtInterceptor();
    }
}

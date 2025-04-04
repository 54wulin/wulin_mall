package com.wulin.wl_mall_tiny.config;


import cn.hutool.core.util.ArrayUtil;
import com.wulin.wl_mall_tiny.component.JwtAuthenticationTokenFilter;
import com.wulin.wl_mall_tiny.component.RestAuthenticationEntryPoint;
import com.wulin.wl_mall_tiny.component.RestfulAccessDeniedHandler;
import com.wulin.wl_mall_tiny.domain.AdminUserDetails;
import com.wulin.wl_mall_tiny.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;


/*
*SpringSecurity的配置
* */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private UmsAdminService adminService;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    @Autowired
    private IgnoreUrlsConfig ignoreUrlsConfig;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        List<String > urls = ignoreUrlsConfig.getUrls();
        String[] urlArray = ArrayUtil.toArray(urls, String.class);
        httpSecurity.csrf()//由于使用的是jwt，不需要使用csrf
                .disable()
                .sessionManagement()//基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, urlArray)//允许对于网站静态资源的无授权访问
                .permitAll()
                .antMatchers(HttpMethod.POST, urlArray)//允许对于网站静态资源的无授权访问
                .permitAll()
                .antMatchers("/admin/login")//对登陆注册要允许匿名访问
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)//跨域请求会先进行一次options请求
                .permitAll()
                .anyRequest()//除上面外的所有请求全部需要鉴权认证
                .authenticated();

        //禁用缓存
        httpSecurity.headers().cacheControl();
        //添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        //添加自定义未授权和未登陆结果返回
        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthenticationEntryPoint);
        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        //获取登陆用户信息
        return username ->{
            AdminUserDetails admin = adminService.getAdminByUsername(username);
            if(admin != null){
                return admin;
            }
            throw new UsernameNotFoundException("用户名或密码错误");
        };
    }
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter(){
        return new JwtAuthenticationTokenFilter();
    }
}

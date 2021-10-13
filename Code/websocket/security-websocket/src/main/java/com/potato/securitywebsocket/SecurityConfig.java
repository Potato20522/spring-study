package com.potato.securitywebsocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 设置密码编码的配置参数，这里设置为 NoOpPasswordEncoder，不配置密码加密，方便测试。
     *
     * @return 密码编码实例
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    /**
     * 设置权限认证参数，这里用于创建两个用于测试的用户信息。
     * 测试用户名/密码1：mydlq1/123456
     * 测试用户名/密码2：mydlq2/123456
     * @param auth SecurityBuilder 用于创建 AuthenticationManager。
     * @throws Exception 抛出的异常
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("mydlq1")
                .password("123456")
                .roles("admin")
                .and()
                .withUser("mydlq2")
                .password("123456")
                .roles("admin");
    }

    /**
     * 设置 HTTP 安全相关配置参数
     *
     * @param http HTTP Security 对象
     * @throws Exception 抛出的异常信息
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .permitAll();
    }

}
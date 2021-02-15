package com.haoxuan.demo.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)// 设置自定义的userDetailsService
                .passwordEncoder(passwordEncoder());
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        //System.out.println(NoOpPasswordEncoder.getInstance());
        return NoOpPasswordEncoder.getInstance();// 使用不使用加密算法保持密码

//        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().and()
                .authorizeRequests()
                .antMatchers("/user/create").permitAll()
                .antMatchers("/user/hello").permitAll()
                .antMatchers("/user/getInfo").authenticated()
                .antMatchers("/user/updateInfo").authenticated()
                .antMatchers("/book/all").permitAll()
                .antMatchers("/book/add").authenticated()
                .antMatchers("/book/queryBookById").authenticated()
                .antMatchers("/book/deleteBookById").authenticated()
                //.anyRequest().permitAll() // 没有定义的请求，所有的角色都可以访问（tmp也可以）。
                .and()
                .csrf().disable()
                .httpBasic()
                .and()
                .logout()
                .permitAll();
    }
}
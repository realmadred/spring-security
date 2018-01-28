package com.feng.security.config;

import com.feng.security.filter.ValidateImageCodeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ValidateImageCodeFilter imageCodeFilter(){
        return new ValidateImageCodeFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.httpBasic().and().authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(imageCodeFilter(), UsernamePasswordAuthenticationFilter.class)
            .formLogin().loginPage("/user/toLogin").permitAll()
            .loginProcessingUrl("/user/login").permitAll()
            .and().authorizeRequests()
            .antMatchers("/css/**", "/js/**","/html/login.html",
                    "/lib/**","/images/**", "/webjars/**",
                    "**/favicon.ico","/image/code").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable();
    }
}

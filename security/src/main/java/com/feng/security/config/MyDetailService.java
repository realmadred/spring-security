package com.feng.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyDetailService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyDetailService.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        final String password = passwordEncoder.encode("123456");
        LOGGER.info(password);
        return new User(s, password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin,feng"));
    }
}

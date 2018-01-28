package com.feng.security.filter;

import com.feng.security.common.Constant;
import com.feng.security.exception.ValidateException;
import com.feng.security.validate.ImageCode;
import org.apache.catalina.manager.util.SessionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * 验证码过滤器
 */
public class ValidateImageCodeFilter extends OncePerRequestFilter {

    private String[] shouldValidateUrls = {"/user","user/*"};

    // 路径匹配
    AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        for (int i = 0; i < shouldValidateUrls.length; i++) {
            if (pathMatcher.match(shouldValidateUrls[i], requestURI)){
                if(validate(request)){
                    break;
                }
            }
        }
        filterChain.doFilter(request,response);
    }

    private boolean validate(HttpServletRequest request) throws ServletRequestBindingException {
        final HttpSession session = request.getSession();
        final Object attribute = session.getAttribute(Constant.SESSION_IMAGE_CODE);

        // 获取请求里带的验证码
        final String requestImageCode = ServletRequestUtils.getStringParameter(request, "imageCode");

        // 校验
        if (StringUtils.isEmpty(requestImageCode)){
            throw new ValidateException("验证码为空！");
        }
        if (attribute == null){
            throw new ValidateException("验证码失效！");
        }
        ImageCode imageCode = (ImageCode) attribute;
        final long expire = imageCode.getExpire();
        if (System.currentTimeMillis() > expire){
            throw new ValidateException("验证码失效！");
        }
        if (!Objects.equals(imageCode.getCode(),requestImageCode)){
            throw new ValidateException("验证码不正确！");
        }
        return true;
    }
}

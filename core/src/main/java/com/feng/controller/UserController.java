package com.feng.controller;

import com.feng.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private static final String HTML = ".html";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 经过security的请求会保存在session中
    private RequestCache requestCache = new HttpSessionRequestCache();

    // 重定向策咯
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @GetMapping("/all")
    public ModelMap getAll(){
        final String sql = "SELECT * FROM sys_users";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        ModelMap modelMap = new ModelMap();
        modelMap.addAllAttributes(maps);
        return modelMap;
    }


    @GetMapping("/me")
    public Authentication getMe(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/{id}")
    public ModelMap getById(@PathVariable("id") Integer id){
        final String sql = "SELECT * FROM sys_users WHERE id = ?";
        Map<String, Object> queryForMap = jdbcTemplate.queryForMap(sql,id);
        ModelMap modelMap = new ModelMap();
        modelMap.addAllAttributes(queryForMap);
        return modelMap;
    }

    @GetMapping("/name")
    public ModelMap getByName(String name){
        final String sql = "SELECT * FROM sys_users WHERE username = ?";
        Map<String, Object> queryForMap = jdbcTemplate.queryForMap(sql,name);
        ModelMap modelMap = new ModelMap();
        modelMap.addAllAttributes(queryForMap);
        return modelMap;
    }

    @GetMapping("/testError")
    public ModelMap testError(){
        throw new MyException("testErrorMsg");
    }

    @GetMapping("/toLogin")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> toLogin(HttpServletRequest req, HttpServletResponse res){
        LOGGER.info("toLogin...");
        final SavedRequest request = requestCache.getRequest(req, res);
        if (request != null) {
            final String redirectUrl = request.getRedirectUrl();
            LOGGER.info("redirectUrl:{}",redirectUrl);
            if (StringUtils.endsWithIgnoreCase(redirectUrl, HTML)){
                try {
                    redirectStrategy.sendRedirect(req,res,"/html/login.html");
                } catch (IOException e) {
                    LOGGER.error("sendRedirect error!");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("需要登陆！");
    }

}

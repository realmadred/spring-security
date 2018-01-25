package com.feng.controller;

import com.feng.exception.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/all")
    public ModelMap getAll(){
        final String sql = "SELECT * FROM sys_users";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        ModelMap modelMap = new ModelMap();
        modelMap.addAllAttributes(maps);
        return modelMap;
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

}

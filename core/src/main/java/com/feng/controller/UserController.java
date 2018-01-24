package com.feng.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/all")
    public ModelMap getAll(){
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("name","zhangSan");
        modelMap.addAttribute("age",22);
        modelMap.addAttribute("sex","女");
        modelMap.addAttribute("address","广东东莞");
        return modelMap;
    }

}

package com.bjpowernode.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
public class IndexController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("find")
    @ResponseBody
    public List<String> find(){
        List<String> list = new ArrayList<>();
        list.add("111");
        list.add("222");
        System.out.println("请求成功");
        return list;
    }
}

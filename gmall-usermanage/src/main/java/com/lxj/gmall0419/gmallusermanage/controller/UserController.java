package com.lxj.gmall0419.gmallusermanage.controller;


import com.lxj.gmall0419.bean.UserInfo;
import com.lxj.gmall0419.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @RequestMapping("findAll")
    @ResponseBody
    public List<UserInfo> findAll(){
        return userService.getUserInfoListAll();
    }

    @RequestMapping("add")
    @ResponseBody
    public void add(){
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("test@qq.com");
        userInfo.setLoginName("test");
        userInfo.setName("testQQ");
        userInfo.setPasswd("test");
        userService.addUser(userInfo);
    }

    @RequestMapping("upd")
    @ResponseBody
    public void upd(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId("4");
        userInfo.setNickName("testAdmin-test");
        userService.updateUser(userInfo);
    }

    @RequestMapping("upd1")
    @ResponseBody
    public void upd1(String name){
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName("testAdmin-test");
        userService.updateUserByName(name,userInfo);
    }

    @RequestMapping("del")
    @ResponseBody
    public void del(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId("4");
        userService.delUser(userInfo);
    }
}

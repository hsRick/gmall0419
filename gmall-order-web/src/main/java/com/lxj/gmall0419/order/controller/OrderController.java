package com.lxj.gmall0419.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lxj.gmall0419.bean.UserAddress;
import com.lxj.gmall0419.bean.UserInfo;
import com.lxj.gmall0419.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class OrderController {

    @Reference
    private UserService userService;

    @RequestMapping("/test")
    public String test(){
        // 返回一个视图名称叫index.html
        return "index";
    }

    @RequestMapping("/trade")
    @ResponseBody
    public List<UserInfo> trade(){
        List<UserInfo> userInfoListAll = userService.getUserInfoListAll();
        return userInfoListAll;
    }

    @RequestMapping("/dress/{id}")
    @ResponseBody
    public List<UserAddress> dress(@PathVariable String id){
        List<UserAddress> userAddressList = userService.getUserAddressList(id);
        return userAddressList;
    }
}

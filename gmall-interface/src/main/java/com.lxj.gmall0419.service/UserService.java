package com.lxj.gmall0419.service;

import com.lxj.gmall0419.bean.UserAddress;
import com.lxj.gmall0419.bean.UserInfo;

import java.util.List;

public interface UserService {

    List<UserInfo> getUserInfoListAll();

    void addUser(UserInfo userInfo);

    void updateUser(UserInfo userInfo);

    void updateUserByName(String name,UserInfo userInfo);

    void delUser(UserInfo userInfo);

     List<UserAddress> getUserAddressList(String userId);

}
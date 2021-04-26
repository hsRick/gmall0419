package com.lxj.gmall0419.gmallusermanage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lxj.gmall0419.bean.UserAddress;
import com.lxj.gmall0419.bean.UserInfo;
import com.lxj.gmall0419.gmallusermanage.mapper.UserAddressMapper;
import com.lxj.gmall0419.gmallusermanage.mapper.UserInfoMapper;
import com.lxj.gmall0419.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    private UserAddressMapper userAddressMapper;
    @Override
    public List<UserInfo> getUserInfoListAll() {
        return userInfoMapper.selectAll();
    }

    @Override
    public void addUser(UserInfo userInfo) {

        userInfoMapper.insert(userInfo);
    }

    @Override
    public void updateUser(UserInfo userInfo) {
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }

    @Override
    public void updateUserByName(String name, UserInfo userInfo) {
        Example example = new Example(UserInfo.class);
        example.createCriteria().andEqualTo("name",name);
        userInfoMapper.updateByExampleSelective(userInfo,example);
    }

    @Override
    public void delUser(UserInfo userInfo) {
        userInfoMapper.deleteByPrimaryKey(userInfo);
    }


    @Override
    public List<UserAddress> getUserAddressList(String userId) {
        // 调用mapper
        // select * from userAddress where userId=?
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

}
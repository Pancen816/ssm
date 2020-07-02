package com.bruce.service;

import com.bruce.entity.User;

/**
 * 用户模块  service 接口
 *
 * @Project ssm
 * @Package com.bruce.service
 * @ClassName UserService
 * @Author Bruce
 * @Date 2020/6/29 0:33
 * @Version 1.0
 **/
public interface UserService {

    void checkUsername(String username);

    void register(User user);

    User login(String username,String password);

}

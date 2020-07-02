package com.bruce.service.impl;

import com.bruce.constant.SsmConstant;
import com.bruce.entity.User;
import com.bruce.enums.ExceptionInfoEnum;
import com.bruce.exception.SsmException;
import com.bruce.mapper.UserMapper;
import com.bruce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 用户模块 service 实现类
 * @project: ssm
 * @package: com.bruce.service.impl
 * @author: Bruce
 * @data: 2020/6/2820:37
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 异步校验用户名
     * @author Bruce
     * @date 2020/6/29 13:37
     * @param username   用户名
     * @return void
     */
    @Override
    public void checkUsername(String username) {
        // 封装查询条件
        User user = new User();
        user.setUsername(username);
        // 调用userMapper查询
        int count = userMapper.selectCount(user);
        // 判断返回结果
        if (count != 0){
            log.info("【异步校验用户名】用户名不可用！username={}",username);
            throw new SsmException(ExceptionInfoEnum.USER_CHECKERED_ERROR);
        }
    }

    /**
     * 注册
     * @author Bruce
     * @date 2020/6/29 13:38
     * @param user   用户对象
     * @return void
     */
    @Override
    public void register(User user) {
        // 生成一个盐
        String salt = UUID.randomUUID().toString();
        // 对密码进行加密和加盐
        String newPassword = new Md5Hash(user.getPassword(), salt, SsmConstant.HASH_ITERATIONS).toString();
        // 封装数据
        user.setPassword(newPassword);
        user.setSalt(salt);
        // 调用userMapper进行存储
        int count = userMapper.insertSelective(user);
        // 判断返回结果
        if (count != 1){
            log.info("【注册账号】注册账号失败！user={}",user);
            throw new SsmException(ExceptionInfoEnum.USER_REGISTER_ERROR);
        }
    }

    /**
     * 登录
     * @author Bruce
     * @date 2020/6/29 17:38
     * @param username  用户名
     * @param password  密码
     * @return user
     */
    @Override
    public User login(String username, String password) {
        // 根据用户名查询用户信息
        User param = new User();
        param.setUsername(username);
        User user = userMapper.selectOne(param);
        // 如果用户信息为null，提示用户不存在
        if (user == null){
            log.info("【登录功能】用户名不存在！username = {}",username);
            throw new SsmException(ExceptionInfoEnum.USER_USERNAME_ERROR);
        }
        // 将用户输入的密码进行加密加盐
        String newPassword = new Md5Hash(password, user.getSalt(), SsmConstant.HASH_ITERATIONS).toString();
        // 判断用户密码是否正确，错误则提示密码错误
        if (!user.getPassword().equals(newPassword)){
            log.info("【登录功能】密码错误！user = {},username = {},password = {}",user,username,newPassword);
            throw new SsmException(ExceptionInfoEnum.USER_PASSWORD_ERROR);
        }
        // 返回用户信息
        return user;
    }
}

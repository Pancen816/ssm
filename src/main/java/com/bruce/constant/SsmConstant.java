package com.bruce.constant;

/**
 * 定义常量类
 * @project: ssm
 * @package: com.bruce.constant
 * @author: Bruce
 * @data: 2020/6/2823:02
 */
public interface SsmConstant {

    // 用户发送信息正确的验证码存放在session域中的key
    String USER_CODE = "user-code";

    // 密码加密次数
    int HASH_ITERATIONS = 1024;

    // 设置到session域中的用户信息
    String USER_INFO = "user_info";


}

package com.bruce.enums;

import lombok.Getter;

@Getter
public enum ExceptionInfoEnum {
    PARAM_ERROR(1,"参数不合法！"),
    USER_USERNAME_ERROR(10,"用户名不存在！"),
    USER_CHECKERED_ERROR(11,"用户名不可用！"),
    USER_REGISTER_ERROR(13,"注册账号失败！"),
    USER_CAPTCHA_ERROR(14,"验证码错误！"),
    USER_PASSWORD_ERROR(15,"密码错误！"),
    ITEM_ADD_ERROR(31,"添加商品失败！"),
    ITEM_DELETE_ERROR(32,"删除商品失败！"),
    ITEM_UPDATE_ERROR(33,"修改商品失败！"),
    PIC_GREATER(41,"图片内存过大！"),
    PIC_TYPE_ERROE(42,"图片类型不正确！"),
    PIC_BREAK(43,"图片已损坏！");

    private Integer code;

    private String msg;

    ExceptionInfoEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

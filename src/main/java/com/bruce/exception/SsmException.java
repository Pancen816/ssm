package com.bruce.exception;

import com.bruce.enums.ExceptionInfoEnum;
import lombok.Getter;

/**
 * 异常定义类
 * @project: ssm
 * @package: com.bruce.exception
 * @author: Bruce
 * @data: 2020/6/2820:52
 */
@Getter
public class SsmException extends RuntimeException{

    private Integer code;

    public SsmException( Integer code,String message) {
        super(message);
        this.code = code;
    }

    public SsmException(ExceptionInfoEnum infoEnum) {
        super(infoEnum.getMsg());
        this.code = infoEnum.getCode();
    }
}

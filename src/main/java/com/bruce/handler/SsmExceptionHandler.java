package com.bruce.handler;

import com.bruce.exception.SsmException;
import com.bruce.vo.ResultVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理器
 * @project: ssm
 * @package: com.bruce.handler
 * @author: Bruce
 * @data: 2020/6/2821:03
 */
@ControllerAdvice
public class SsmExceptionHandler {

    @ExceptionHandler({SsmException.class})
    @ResponseBody
    public ResultVO ssmException(SsmException ex){

        return new  ResultVO(ex.getCode(),ex.getMessage(),null);

    }

}

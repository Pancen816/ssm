package com.bruce.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回结果类型实体类
 * @project: ssm
 * @package: com.bruce.vo
 * @author: Bruce
 * @data: 2020/6/2820:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO {

    private Integer code;

    private String msg;

    private Object data;
}

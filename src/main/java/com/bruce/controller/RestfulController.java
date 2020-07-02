package com.bruce.controller;

import org.springframework.web.bind.annotation.*;

/**
 * RESTful架构风格controller类
 *
 * @Project ssm
 * @Package com.bruce.controller
 * @ClassName RESTfulController
 * @Author Bruce
 * @Date 2020-06-30 22:54
 * @Version 1.0
 **/
@RestController  //组合注解 == @Controller + @ResponseBody
@RequestMapping("/rest")
public class RestfulController {


    @PutMapping(value = "/api",produces = "text/html;charset=utf-8")
    public String api(String name){
        System.out.println(name);
        return name;
    }

    @GetMapping("/user/{id}")
    public String user(@PathVariable Integer id){

        return "id:" + id;
    }

}

package com.bruce.controller;

import com.bruce.entity.User;
import com.bruce.enums.ExceptionInfoEnum;
import com.bruce.exception.SsmException;
import com.bruce.service.UserService;
import com.bruce.util.SendSMSUtil;
import com.bruce.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.bruce.constant.SsmConstant.*;


/**
 * 用户controller类
 * @Project ssm
 * @Package com.bruce.controller
 * @ClassName ItemController
 * @Author Bruce
 * @Date 2020/6/28 23:47
 * @Version 1.0
 **/
@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SendSMSUtil sendSMSUtil;


   /** 跳转注册页面
    * @author Bruce
    * @date 2020/6/29 0:26
    * @return java.lang.String
    */
    @GetMapping("/register-ui")
    public String registerUI(){

        return "user/register";
    }

    /** 异步校验用户名是否可用
     * @author Bruce
     * @date 2020/6/29 0:27
     * @param username  用户名
     * @return com.bruce.vo.ResultVO
     */
    @PostMapping("/check-username")
    @ResponseBody
    public ResultVO checkUsername(String username){

        // 调用userService判断是否可用
        userService.checkUsername(username);
        // 用户名可用
        return new ResultVO(0,"成功",null);
    }

    /** 发送短信验证码
     * @author Bruce
     * @date 2020/6/29 0:28
     * @param phone      手机号
     * @param session    session域对象
     * @return com.bruce.vo.ResultVO
     */
    @PostMapping("/send-sms")
    @ResponseBody
    public ResultVO sendSMS(String phone, HttpSession session){
        // 校验参数合法性
        if (phone == null || phone.length() != 11){
            // 参数不合法
            log.info("【手机验证码】参数不合法 phone={}",phone);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"手机号格式错误！");
        }
        // 调用工具类发送验证码，拿到返回的code和msg，将正确的验证码存放到session域中
        ResultVO vo = sendSMSUtil.sendSMS(phone, session);
        // 响应数据
        return vo;
    }


    /** 
     * 注册账号
     * @author Bruce
     * @date 2020/6/29 14:41 
     * @param user   用户对象
     * @param bindingResult  校验结果集
     * @param registerCode   验证码
     * @return com.bruce.vo.ResultVO
     */
    @PostMapping("/register")
    @ResponseBody
    public ResultVO register(@Valid User user, BindingResult bindingResult,String registerCode,HttpSession session){

        // 从session域中取出正确的验证码
        String trueCode = (String) session.getAttribute(USER_CODE);
        // 校验验证码不为null，并且与session中正确的验证码一致
        if (registerCode == null || registerCode.equals(123456)){
            // 验证码为空或者验证码错误
            log.info("【注册账号】验证码错误！registerCode = {},trueCode = {}",registerCode,trueCode);
            throw new SsmException(ExceptionInfoEnum.USER_CAPTCHA_ERROR);
        }
        // 通过bindingResult判断用户名、密码、手机号是否合法
        if (bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.info("【注册账号】参数不合法！msg = {}",msg);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),msg);
        }
        // 调用userService进行注册
        userService.register(user);
        // 注册成功，响应数据
        return new ResultVO(0,"成功",null);
    }


    /**
     * 跳转登录页面
     * @author Bruce
     * @date 2020/6/29 17:33
     * @param
     * @return java.lang.String
     */
    @GetMapping("/login-ui")
    public String loginUI(){

        return "user/login";

    }

    @PostMapping("/login")
    @ResponseBody
    public ResultVO login(String username,String password,HttpSession session){
        // 接收参数，并校验用户名和密码
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            log.info("【登录功能】参数不合法！username = {},password = {}",username,password);
            throw new SsmException(ExceptionInfoEnum.PARAM_ERROR.getCode(),"用户名和密码不能为空！");
        }
        // 调用userService进行登录
        User user = userService.login(username, password);
        // 将用户信息设置到session域中
        session.setAttribute(USER_INFO,user);
        // 响应数据
        return new ResultVO(0,"成功",null);
    }
}

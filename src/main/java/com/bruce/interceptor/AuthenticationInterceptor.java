package com.bruce.interceptor;

import com.bruce.constant.SsmConstant;
import com.bruce.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 认证拦截器...用户权限校验
 *
 * @Project ssm
 * @Package com.bruce.interceptor
 * @ClassName AuthenticationInterceptor
 * @Author Bruce
 * @Date 2020-06-30 14:43
 * @Version 1.0
 **/
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 通过request获取session
        HttpSession session = request.getSession();
        // 通过session获取user对象
        User user = (User) session.getAttribute(SsmConstant.USER_INFO);
        // 判断用户信息是否为null
        if (user == null){
            // 用户未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/user/login-ui");
            return false;
        }else {
            // 用户已登录，放行
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

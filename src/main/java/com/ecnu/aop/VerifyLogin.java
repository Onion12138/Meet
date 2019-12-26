package com.ecnu.aop;

import com.ecnu.enums.ResultEnum;
import com.ecnu.exception.MyException;
import com.ecnu.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author onion
 * @date 2019/12/15 -9:50 下午
 */
@Aspect
@Component
@Order(1)
public class VerifyLogin {
    @Pointcut("within(com.ecnu.controller..*) && !@annotation(com.ecnu.annotation.LoginRequired)")
    public void verifyLoginPointcut(){}
    @Before("verifyLoginPointcut()")
    public void verifyLogin(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) attributes;
        HttpServletRequest request = sra.getRequest();
        String token = request.getHeader("user_token");
        if (StringUtils.isEmpty(token)){
            throw new MyException(ResultEnum.MUST_LOGIN);
        }
        Claims claims = JwtUtil.parseJwt(token);
        if ("admin".equals(claims.get("role", String.class))){
            request.setAttribute("admin_claims", claims);
        }
        if ("user".equals(claims.get("role", String.class))){
            request.setAttribute("user_claims", claims);
        }
    }
}

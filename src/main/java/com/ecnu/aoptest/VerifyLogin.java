package com.ecnu.aoptest;

import com.ecnu.enums.ResultEnum;
import com.ecnu.exception.MyException;
import com.ecnu.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Pointcut("within(com.ecnu.controllertest..*) && !@annotation(com.ecnu.annotation.LoginRequired)")
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
        if (redisTemplate.opsForValue().get(token) == null) {
            throw new MyException(ResultEnum.LOGOUT);
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

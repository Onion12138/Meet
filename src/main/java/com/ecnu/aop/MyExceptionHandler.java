package com.ecnu.aop;

import com.ecnu.enums.ResultEnum;
import com.ecnu.exception.MyException;
import com.ecnu.utils.JwtUtil;
import com.ecnu.vo.ResultEntity;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author onion
 * @date 2019/12/10 -11:14 下午
 */
@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResultEntity exception(Exception e){
        e.printStackTrace();
        return ResultEntity.fail(e.getMessage());
    }
    @Pointcut("within(com.ecnu.controller..*) && !@annotation(com.ecnu.annotation.LoginRequired)")
    public void verifyLoginPointcut(){}
    @Pointcut("@annotation(com.ecnu.annotation.AdminOnly)")
    public void verifyAdminPointcut(){}
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

    @Before("verifyAdminPointcut()")
    public void verifyAdmin(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) attributes;
        HttpServletRequest request = sra.getRequest();
        if (request.getAttribute("admin_claims") == null){
            throw new MyException(ResultEnum.NO_AUTHORITY);
        }
    }
}

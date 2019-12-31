package com.ecnu.aoptest;

import com.ecnu.enums.ResultEnum;
import com.ecnu.exception.MyException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author onion
 * @date 2019/12/15 -9:51 下午
 */
@Aspect
@Component
@Order(2)
public class VerifyAdmin {
    @Pointcut("@annotation(com.ecnu.annotation.AdminOnly)")
    public void verifyAdminPointcut(){}
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

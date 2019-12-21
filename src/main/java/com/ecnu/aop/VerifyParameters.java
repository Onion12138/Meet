package com.ecnu.aop;

import com.ecnu.exception.MyException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author onion
 * @date 2019/12/15 -9:57 下午
 */
@Aspect
@Order(3)
public class VerifyParameters {
    @Pointcut("@annotation(com.ecnu.annotation.VerifyParams)")
    public void verifyParamsPoint(){}
    @Before("verifyParamsPoint()")
    public void verifyParams(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        BindingResult result = (BindingResult) args[1];
        if (result.hasErrors()){
            StringBuilder sb = new StringBuilder();
            result.getAllErrors().forEach(e->{
                FieldError error = (FieldError) e;
                String field = error.getField();
                String message = error.getDefaultMessage();
                sb.append(field).append(" : ").append(message).append(' ');
            });
            throw new MyException(sb.toString(), -1);
        }
    }
}

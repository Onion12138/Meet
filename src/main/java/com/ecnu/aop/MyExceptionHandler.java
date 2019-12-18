package com.ecnu.aop;

import com.ecnu.vo.ResultEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}

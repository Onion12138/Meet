package com.ecnu.aop;

import com.ecnu.vo.ResultVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author onion
 * @date 2019/12/10 -11:14 下午
 */
@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ResultVO exception(Exception e){
        e.printStackTrace();
        return ResultVO.fail(e.getMessage());
    }
}

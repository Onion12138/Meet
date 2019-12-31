package com.ecnu.exception;

import com.ecnu.enums.ResultEnum;
import lombok.Getter;

/**
 * @author onion
 * @date 2019/12/10 -11:09 下午
 */
@Getter
public class MyException extends RuntimeException {
    private Integer code;
    public MyException(ResultEnum resultEnums){
        super(resultEnums.getMessage());
        this.code=resultEnums.getCode();
    }
    public MyException(String message, Integer code){
        super(message);
        this.code=code;
    }
}

package com.ecnu.enums;

import lombok.Getter;

/**
 * @author onion
 * @date 2019/12/10 -11:08 下午
 */
@Getter
public class ResultEnum {
    ;
    private int code;
    private String message;
    ResultEnum(int code, String message){
        this.code = code;
        this.message = message;
    }
}

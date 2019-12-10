package com.ecnu.vo;

import com.ecnu.enums.ResultEnum;
import lombok.Data;

/**
 * @author onion
 * @date 2019/12/10 -11:07 下午
 */
@Data
public class ResultEntity {
    private Object data;
    private Integer code;
    private String message;
    private ResultEntity(Object data, Integer code, String message){
        this.data = data;
        this.code = code;
        this.message = message;
    }
    public static ResultEntity succeed(){
        return new ResultEntity(null, 0, "成功");
    }
    public static ResultEntity succeed(Object data){
        return new ResultEntity(data, 0, "成功");
    }
    public static ResultEntity fail(ResultEnum resultEnum){
        return new ResultEntity(null, resultEnum.getCode(), resultEnum.getMessage());
    }
    public static ResultEntity fail(String message){
        return new ResultEntity(null, -1, message);
    }
}


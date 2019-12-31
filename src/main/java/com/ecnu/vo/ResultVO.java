package com.ecnu.vo;

import com.ecnu.enums.ResultEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author onion
 * @date 2019/12/10 -11:07 下午
 */
@Data
public class ResultVO implements Serializable {
    private Object data;
    private Integer code;
    private String message;
    public ResultVO(){ }
    private ResultVO(Object data, Integer code, String message){
        this.data = data;
        this.code = code;
        this.message = message;
    }
    public static ResultVO succeed(){
        return new ResultVO(null, 0, "成功");
    }
    public static ResultVO succeed(Object data){
        return new ResultVO(data, 0, "成功");
    }
    public static ResultVO fail(ResultEnum resultEnum){
        return new ResultVO(null, resultEnum.getCode(), resultEnum.getMessage());
    }
    public static ResultVO fail(String message){
        return new ResultVO(null, -1, message);
    }
}


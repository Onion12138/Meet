package com.ecnu.utils;

import com.ecnu.exception.MyException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author onion
 * @date 2019/12/26 -10:52 下午
 */
public class ParamUtil {
    public static void verifyParam(BindingResult result) {
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

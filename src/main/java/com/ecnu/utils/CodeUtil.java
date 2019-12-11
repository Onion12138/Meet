package com.ecnu.utils;

import java.util.Random;

/**
 * @author onion
 * @date 2019/12/11 -6:38 下午
 */
public class CodeUtil {
    private static final String source = "0123456789";
    public static String getCode(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 6; i++){
            sb.append(source.charAt(random.nextInt(10)));
        }
        return sb.toString();
    }
}

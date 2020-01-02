package com.ecnu.utils;

import java.util.Random;

/**
 * @date 2019/12/11 -6:38 下午
 */
public class KeyUtil {
    public static synchronized String genUniqueKey() {
        Random random = new Random();
        Integer number = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}

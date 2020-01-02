package com.ecnu.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CodeUtilTest {
    @Test
    @DisplayName("测试生成验证码")
    public void testGetCode() {
        String code = CodeUtil.getCode();
        assertTrue(code.matches("\\d{6}"));
    }
}

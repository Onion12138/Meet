package com.ecnu.utils;

import com.ecnu.domain.User;
import com.ecnu.enums.ResultEnum;
import com.ecnu.exception.MyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JwtUtilTest {
    private User user;
    @BeforeEach
    public void init() {
        user = new User();
        user.setAdmin(true);
        user.setEmail("969023014@qq.com");
        user.setNickname("Onion");
    }

    @Test
    @DisplayName("测试生成用户的token")
    public void testCreateJwt() {
        String jwt = JwtUtil.createJwt(user);
        assertFalse(StringUtils.isEmpty(jwt));
    }

    @Test
    @DisplayName("测试传入过期的token，会抛出异常")
    public void testTimeoutTokenThrowsException() {
        String timeoutToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5NjkwMjMwMTRAcXEuY29tIiwiaWF0IjoxNTc3MzcyMTAyLCJyb2xlIjoiYWRtaW4iLCJuaWNrbmFtZSI6Ik9uaW9uIiwiZXhwIjoxNTc3NDU4NTAyfQ.wvXFi8HUpKRgL3716ClY60_jCyvSYscXvNW4E-7bKBU";
        assertThrows(MyException.class, () -> JwtUtil.parseJwt(timeoutToken), ResultEnum.INVALID_TOKEN.getMessage());
    }

    @Test
    @DisplayName("测试传入伪造的token，会抛出异常")
    public void testFakeTokenThrowsException() {
        String fakeToken = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI5NjkwMjMwMTRAcXEuY29tIiwiaWF0IjoxNTc3NzU5NzE2LCJyb2xlIjoiYWRtaW4iLCJuaWNrbmFtZSI6Ik9uaW9uIiwiZXhwIjoxNTc3ODQ2MTE2fQ.j1pdcY_OLeiE-kSTx_pmAek03_XOQ0-8gNFOvHN-FS";
        assertThrows(MyException.class, () -> JwtUtil.parseJwt(fakeToken), ResultEnum.INVALID_TOKEN.getMessage());
    }
}

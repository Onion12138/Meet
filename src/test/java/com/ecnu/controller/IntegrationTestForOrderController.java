package com.ecnu.controller;

import com.ecnu.dao.UserDao;
import com.ecnu.domain.User;
import com.ecnu.request.AvailableTimeRequest;
import com.ecnu.utils.JwtUtil;
import com.ecnu.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public class IntegrationTestForOrderController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TestRestTemplate restTemplate;

    private final static String REQUEST_MAPPING = "/order";

    private final static String SUCCESS_MSG = "成功";

    private final static int OK = 200;

    private final static String testDate = "2020/01/01";

    private final static String testGymId = "1577853371105277671";

    private String tokenForUser;

    private String tokenForAdmin;

    private User testUser;

    private User admin;
    @BeforeEach
    public void choseTestUserAndAdmin() {
        testUser = userDao.findById("leodpen@gmail.com").get();
        admin = userDao.findById("969023014@qq.com").get();
        tokenForUser = JwtUtil.createJwt(testUser);
        tokenForAdmin = JwtUtil.createJwt(admin);
        redisTemplate.opsForValue().set(tokenForAdmin,admin.getEmail(),144, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(tokenForUser,testUser.getEmail(),144, TimeUnit.SECONDS);
    }

    @AfterEach
    public void deleteTokens() {
        redisTemplate.delete(tokenForAdmin);
        redisTemplate.delete(tokenForUser);
    }

    @Test
    @DisplayName("测试查看所有可用的时间，需要登陆")
    @Transactional
    public void testFindAvailableTime(){
        AvailableTimeRequest request = AvailableTimeRequest.builder()
                .date(testDate)
                .gymId(testGymId)
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForUser);
        HttpEntity<AvailableTimeRequest> entity = new HttpEntity<>(request,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(
                REQUEST_MAPPING + "/available",
                entity,
                ResultVO.class
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assert result != null;
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );

    }

}

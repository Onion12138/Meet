package com.ecnu.controller;

import com.ecnu.dao.UserDao;
import com.ecnu.domain.User;
import com.ecnu.request.AvailableTimeRequest;
import com.ecnu.request.OrderCommentRequest;
import com.ecnu.request.OrderRequest;
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
import java.util.HashMap;
import java.util.Map;
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
    public void testFindAvailableTime() {
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
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    // 使用驼峰法即过
    @Test
    @DisplayName("测试查看我的取消订单,需要登陆")
    @Transactional
    public void testMyCanceledOrder() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/myCanceledOrder?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看我的所有订单,需要登陆")
    @Transactional
    public void testMyOrder() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/myOrder?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看我的当前进行中订单,需要登陆")
    @Transactional
    public void testMyCurrentOrder() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/myCurrentOrder?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看我的未来订单,需要登陆")
    @Transactional
    public void testMyFutureOrder() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/myFutureOrder?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看我的过去的订单,需要登陆")
    @Transactional
    public void testMyPastOrder() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/myPastOrder?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看gym的type来查我的订单,需要登陆")
    @Transactional
    public void testFindMyOrdersByGymType() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Object> map = new HashMap<>();
        map.put("type","篮球");
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/myOrderByGym?type={type}&page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试添加订单,需要登陆")
    @Transactional
    public void testAddOrder() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        OrderRequest request = OrderRequest.builder()
                .date("2020/01/04")
                .startTime(26)
                .endTime(40)
                .gymId(testGymId)
                .build();
        HttpEntity<OrderRequest> entity = new HttpEntity<>(request,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(
                REQUEST_MAPPING + "/addOrder",
                entity,
                ResultVO.class
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试添加评论,需要登陆(默认好评)")
    @Transactional
    public void testComment() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        OrderCommentRequest request = OrderCommentRequest.builder()
                .comment("德玛西亚用不为奴")
                .orderId("1577863710286417087")
                .score(5)
                .build();

        HttpEntity<OrderCommentRequest> entity = new HttpEntity<>(request,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(
                REQUEST_MAPPING + "/comment",
                entity,
                ResultVO.class
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试取消订单,需要登陆")
    @Transactional
    public void testCancelOrder() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,String> map = new HashMap<>();
        map.put("orderId","1577863710286417087");

        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(
                REQUEST_MAPPING + "/cancelMyOrder?orderId={orderId}",
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看所有订单,需要管理员登陆")
    @Transactional
    public void testSeeAllOrderByAdmin() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/allOrder?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看当前进行中所有订单,需要管理员登陆")
    @Transactional
    public void testAllCurrentOrder() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/currentOrder?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看未来所有订单,需要管理员登陆")
    @Transactional
    public void testAllFutureOrder() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/futureOrder?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看过去所有订单,需要管理员登陆")
    @Transactional
    public void testAllPastOrder() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/pastOrder?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看通过gym的type查找订单,需要管理员登陆")
    @Transactional
    public void testFindAllOrdersByGymByAdmin() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Object> map = new HashMap<>();
        map.put("type","篮球");
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/gymOrder?type={type}&page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看某个特定用户的订单,需要管理员登陆")
    @Transactional
    public void testFindAllOrdersByEmailByAdmin() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Object> map = new HashMap<>();
        map.put("email",admin.getEmail());
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/userOrder?email={email}&page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> {
                    assertEquals(0,result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试查看所有被取消的订单,需要管理员登陆")
    @Transactional
    public void testFindAllCanceledOrdersByAdmin() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        map.put("page",1);
        map.put("size",5);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/allCanceledOrder?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        assertAll(
                () -> assertEquals(OK,response.getStatusCodeValue()),
                () -> assertEquals(0,result.getCode()),
                () -> assertEquals(SUCCESS_MSG,result.getMessage())
        );
    }



}

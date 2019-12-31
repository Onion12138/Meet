package com.ecnu.controller;

import com.ecnu.dao.UserDao;
import com.ecnu.domain.User;
import com.ecnu.request.UserLoginRequest;
import com.ecnu.request.UserRegisterRequest;
import com.ecnu.enums.ResultEnum;
import com.ecnu.utils.JwtUtil;
import com.ecnu.vo.ResultVO;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public class IntegrationTestForUserController {

    private final static String REQUEST_MAPPING = "/user";

    private final static String SUCCESS_MSG = "成功";

    private final static int OK = 200;

    private final static String used_email = "969023014@qq.com";

    private final static String new_email = "10175101227@stu.ecnu.edn.cn";

    private String tokenForUser;

    private String tokenForAdmin;

    private User testUser;

    private User admin;

//    private final String code = "";

    // web 测试
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserDao userDao;

    @BeforeEach
    public void choseTestUserAndAdmin() {
        testUser = userDao.findById("leodpen@gmail.com").get();
//        testUser.setAdmin(false);
        admin = userDao.findById("969023014@qq.com").get();
//        admin.setAdmin(true);
        tokenForUser = JwtUtil.createJwt(testUser);
//        log.info(tokenForUser);
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
    @DisplayName("邮箱已经被使用，返回提示信息")
    @Transactional
    public void testEmailInUse() {
        ResponseEntity<ResultVO> response = restTemplate.getForEntity( REQUEST_MAPPING + "/check?email={email}", ResultVO.class, used_email);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();

        assertAll(
                () -> assertEquals(OK, statusCode),
                () -> {
                    assert result != null;
                    assertEquals(ResultEnum.EMAIL_IN_USE.getCode(), result.getCode());
                    assertNull(result.getData());
                    assertEquals(ResultEnum.EMAIL_IN_USE.getMessage(), result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("邮箱未被使用，返回成功的ResultEntity")
    @Transactional
    public void testEmailNotInUse(){
        ResponseEntity<ResultVO> response = restTemplate.getForEntity( REQUEST_MAPPING + "/check?email={email}", ResultVO.class, new_email);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();

        assertAll(
                () -> assertEquals(OK, statusCode),
                () ->{
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                    assertNull(result.getData());
                }
        );
    }

    @Test
    @DisplayName("注册邮箱填写格式非法")
    @Transactional
    public void testBadEmailWhenCallRegister(){
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("cewhiuygieoh") // 非法的邮箱
                .password("123456")
                .nickname("onion")
                .code("927ytr")
                .build();
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(REQUEST_MAPPING + "/register",request, ResultVO.class);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();

        assertAll(
                () -> assertEquals(OK,statusCode),
                () -> {
                    assert result != null;
                    assertEquals(-1, result.getCode());
                    // 具体报错信息暂不展示
                    assertNull(result.getData());
                }
        );
    }

    @Test
    @DisplayName("注册姓名长度非法")
    @Transactional
    public void testBadNickNameLengthWhenCallRegister(){
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("yyy@qq.com")
                .password("123456")
                .nickname("too_long_onion_too_long_onion_too_long_onion_too_long_onion")
                .code("927ytr")
                .build();
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(REQUEST_MAPPING + "/register",request, ResultVO.class);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();

        assertAll(
                () -> assertEquals(OK,statusCode),
                () -> {
                    assert result != null;
                    assertEquals(-1, result.getCode());
                    // 具体报错信息暂不展示
                    assertNull(result.getData());
                }
        );
    }

    @Test
    @DisplayName("注册code（验证码）非法——长度不对或不是在合理时间内填写") // 长度不对或不是在合理时间内填写
    @Transactional
    public void testBadPasswordWhenCallRegister() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("yyy@qq.com")
                .password("123456")
                .nickname("onion")
                .code("927ytr")
//                .code("111tttt")
                .build();
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(REQUEST_MAPPING + "/register", request, ResultVO.class);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () -> {
                    assert result != null;
                    assertEquals(-1, result.getCode());
                    // 具体报错信息暂不展示
                    assertNull(result.getData());
                }
        );
    }

    @Test
    @DisplayName("成功注册")
    @Transactional
    public void testSuccessfulRegister(){
        UserRegisterRequest request = UserRegisterRequest.builder()
                .email("leodpen@gmail.com")
                .password("123456")
                .nickname("pf")
                .code("9ppy8r")
                .build();
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(REQUEST_MAPPING + "/register",request, ResultVO.class);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK,statusCode),
                () -> {
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG, result.getMessage());
                    assertNull(result.getData());
                }
        );
    }

    @Test
    @DisplayName("登陆时表格填写错误——邮箱格式不正确或者密码为空")
    @Transactional
    public void testBadLoginWithInvalidForm(){
        UserLoginRequest request = new UserLoginRequest(used_email,"123456_bad_password");
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(REQUEST_MAPPING + "/login",request, ResultVO.class);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK,statusCode),
                () -> {
                    assert result != null;
                    assertEquals(-1, result.getCode());
                    assertNull(result.getData());
                }
        );
    }

    @Test
    @DisplayName("管理员登陆成功")
    @Transactional
    public void testAdminSuccessLogin(){
        UserLoginRequest request = new UserLoginRequest(used_email,"123456");
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(REQUEST_MAPPING + "/login",request, ResultVO.class);
        ResultVO result = response.getBody();
        Map<String, Object> map = (Map<String, Object>) response.getBody().getData();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK,statusCode),
                () -> {
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(request.getEmail(),map.get("email"));
                    assertTrue(map.containsKey("token"));
                    assertTrue(map.containsKey("profile"));
                    assertEquals("admin",map.get("role"));
                    assertTrue(map.containsKey("nickname"));
                }
        );
    }

    @Test
    @DisplayName("普通用户登陆成功")
    @Transactional
    public void testUserSuccessLogin(){
        UserLoginRequest request = new UserLoginRequest(used_email,"123456");
        ResponseEntity<ResultVO> response = restTemplate.postForEntity(REQUEST_MAPPING + "/login",request, ResultVO.class);
        ResultVO result = response.getBody();
        Map<String, Object> map = (Map<String, Object>) response.getBody().getData();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK,statusCode),
                () -> {
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(request.getEmail(),map.get("email"));
                    assertTrue(map.containsKey("token"));
                    assertTrue(map.containsKey("profile"));
                    assertEquals("user",map.get("role"));
                    assertTrue(map.containsKey("nickname"));
                }
        );
    }

    // 按照顺序放最后
    @Test
    @DisplayName("测试注销登陆，无token/token不对")
    @Transactional
    public void testBadLogoutWithoutToken(){
        ResponseEntity<ResultVO> response = restTemplate.getForEntity(REQUEST_MAPPING + "/logout", ResultVO.class);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () -> {
                    assert result != null;
                    assertEquals(-1, result.getCode());
                }
        );

    }


    // 无token情况大同小异，此后将不再单独列出无token情况
    @Test
    @DisplayName("测试注销登陆成功，有正确token")
    public void testSuccessfulLogout(){

        HttpHeaders httpHeaders = new HttpHeaders();
        // 放到最后
        httpHeaders.add("user_token",tokenForUser);

        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/logout",
                HttpMethod.POST,
                new HttpEntity<String>(httpHeaders),
                ResultVO.class);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () -> {
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG, result.getMessage());
                }
        );
    }

    /*** 测试修改个人信息 ***/
    // 无token情况不再列出

    @Test
    @DisplayName("修改个人昵称")
    @Transactional
    public void testModifyNickname(){
        HttpHeaders httpHeaders = new HttpHeaders();
        // 放到最后
        httpHeaders.add("user_token",tokenForUser);
        Map<String,String> map = new HashMap<>();
        map.put("nickname","祖安狂人");
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/modifyNickname?nickname={nickname}",
                HttpMethod.POST,
                entity,
                ResultVO.class,
                map);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () -> {
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    // todo 此处待真实使用下看看
    @Test
    @DisplayName("修改个人头像")
    @Transactional
    public void testModifyProfile() throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        File file = new File("/Users/pengfeng/Desktop/yyy.png");
        MultipartFile multipartFile = new MockMultipartFile(
                "新头像.png", //文件名
                "yyy.png", //originalName 相当于上传文件在客户机上的文件名
                "application/octet-stream",        //ContentType.APPLICATION_OCTET_STREAM.toString(), //文件类型
                new FileInputStream(file) //文件流
        );
        httpHeaders.add("user_token",tokenForUser);
        httpHeaders.add("Content-Type","multipart/form-data");
        Map<String, MultipartFile> map = new HashMap<>();
        map.put("file", multipartFile);
//        HttpEntity<Map<String,MultipartFile>> entity = new HttpEntity<>(map,httpHeaders);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/uploadProfile",
                HttpMethod.POST,
                entity,
                ResultVO.class,
                map);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () -> {
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("修改个人密码")
    @Transactional
    public void testModifyPassword(){
        HttpHeaders httpHeaders = new HttpHeaders();
        // 放到最后
        httpHeaders.add("user_token",tokenForUser);
        Map<String,String> map = new HashMap<>();
        map.put("password","654321");
        map.put("code","ptr521");
        HttpEntity<Map<String,String>> entity = new HttpEntity<>(map,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/modifyPassword",
                HttpMethod.POST,
                entity,
                ResultVO.class);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () -> {
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );

    }

    @Test
    @DisplayName("测试发送验证码")
    @Transactional
    public void testSendEmailForCode(){

        Map<String,String> map = new HashMap<>();
        map.put("email",used_email);
        ResponseEntity<ResultVO> response = restTemplate.getForEntity(REQUEST_MAPPING + "/sendCode?email={email}", ResultVO.class, map);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () ->{
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试管理员查看所有用户")
    public void testFindAllUsersByAdmin(){
        HttpHeaders httpHeaders = new HttpHeaders();
        // 放到最后
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        // 默认使用这个配置
        map.put("page",1);
        map.put("size",20);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(REQUEST_MAPPING + "/findAllUsers?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () ->{
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                    assertEquals(1,((LinkedHashMap)result.getData()).get("pageNum"));
                    assertEquals(20,((LinkedHashMap)result.getData()).get("pageSize"));
                }
        );
    }

    @Test
    @DisplayName("测试管理员查看所有禁用的用户")
    public void testFindAllDisabledUsersByAdmin(){
        HttpHeaders httpHeaders = new HttpHeaders();
        // 放到最后
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Integer> map = new HashMap<>();
        // 默认使用这个配置
        map.put("page",1);
        map.put("size",20);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);
        ResponseEntity<ResultVO> response = restTemplate.exchange(REQUEST_MAPPING + "/findAllDisabledUsers?page={page}&size={size}",
                HttpMethod.GET,
                entity,
                ResultVO.class,
                map);
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () ->{
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                    assertEquals(1,((LinkedHashMap) result.getData()).get("pageNum"));
                    assertEquals(20,((LinkedHashMap) result.getData()).get("pageSize"));
                }
        );
    }

    @Test
    @DisplayName("测试管理员禁用用户")
    @Transactional
    public void testDisableAccountByAdmin(){
        HttpHeaders httpHeaders = new HttpHeaders();
        // 放到最后
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,String> map = new HashMap<>();
        map.put("userId",testUser.getEmail());
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);

        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/disableAccount?userId={userId}",
                HttpMethod.POST,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () ->{
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    @Test
    @DisplayName("测试管理员取消用户禁用")
    @Transactional
    public void testEnableAccountByAdmin(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,String> map = new HashMap<>();
        map.put("userId",testUser.getEmail());
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);

        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/enableAccount?userId={userId}",
                HttpMethod.POST,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () ->{
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );
    }

    // todo have some bugs which I haven't found yet here??
    @Test
    @DisplayName("更新信誉值")
    @Transactional
    public void testUpdateCredit(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("user_token",tokenForAdmin);
        Map<String,Object> map = new HashMap<>();
        map.put("userId",testUser.getEmail());
        map.put("credit",20);
        HttpEntity<String> entity = new HttpEntity<>(null,httpHeaders);

        ResponseEntity<ResultVO> response = restTemplate.exchange(
                REQUEST_MAPPING + "/updateCredit?userId={userId}&credit={credit}",
                HttpMethod.POST,
                entity,
                ResultVO.class,
                map
        );
        ResultVO result = response.getBody();
        int statusCode = response.getStatusCode().value();
        assertAll(
                () -> assertEquals(OK, statusCode),
                () ->{
                    assert result != null;
                    assertEquals(0, result.getCode());
                    assertEquals(SUCCESS_MSG,result.getMessage());
                }
        );

    }

}

package com.ecnu.controller;

import com.ecnu.MeetHereApplication;
import com.ecnu.request.UserRegisterRequest;
import com.ecnu.enums.ResultEnum;
import com.ecnu.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT,classes = MeetHereApplication.class)
@Slf4j
public class IntegrationTestForUserController {

    private final static String REQUEST_MAPPING = "/user";

    private final static String SUCCESS_MSG = "成功";

    private final static int OK = 200;

    private final static String used_email = "969023014@qq.com";

    private final static String new_email = "10175101227@stu.ecnu.edn.cn";

    private final static String password = "123456";

    private final static String nickname = "onion";

//    private final String code = "";

    // web 测试
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    @DisplayName("邮箱已经被使用，返回提示信息")
    public void testEmailInUse(){
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
    public void testBadNickNameLengthWhenCallRegister(){
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








}

package com.ecnu.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.domain.User;
import com.ecnu.dto.UserLoginRequest;
import com.ecnu.dto.UserRegisterRequest;
import com.ecnu.enums.ResultEnum;
import com.ecnu.service.UserService;
import com.ecnu.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private String email;
    private String newEmail;
    private String token;
    private String adminToken;
    @BeforeEach
    public void before() {
        email = "969023014@qq.com";
        newEmail = "10175101227@stu.ecnu.edn.cn";
        User user = new User();
        User admin = new User();
        user.setEmail(email);
        user.setNickname("onion");
        user.setAdmin(false);
        token = JwtUtil.createJwt(user);
        admin.setEmail(email);
        admin.setNickname(user.getNickname());
        admin.setAdmin(true);
        adminToken = JwtUtil.createJwt(admin);
        redisTemplate.opsForValue().set(token, email);
        redisTemplate.opsForValue().set(adminToken, email);
    }
    @Test
    @DisplayName("测试已经注册的邮箱，会返回已经注册的提示信息")
    public void testEmailInUse () throws Exception {
        when(userService.checkEmail(anyString())).thenReturn(false);
        ResultActions perform = mockMvc.perform(get("/user/check").param("email", email));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(ResultEnum.EMAIL_IN_USE.getCode()));
        perform.andExpect(jsonPath("$.message" ).value(ResultEnum.EMAIL_IN_USE.getMessage()));
        verify(userService).checkEmail(anyString());
    }
    @Test
    @DisplayName("测试未注册的邮箱，会返回已经注册的提示信息")
    public void testNewEmailInUse () throws Exception {
        when(userService.checkEmail(anyString())).thenReturn(true);
        ResultActions perform = mockMvc.perform(get("/user/check").param("email", newEmail));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).checkEmail(anyString());
    }
    @Test
    @DisplayName("测试注册")
    public void testRegister() throws Exception {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setCode("123456");
        request.setEmail(email);
        request.setNickname("onion");
        request.setPassword("123456");
        ResultActions perform = mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(request)));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).register(any());
    }
    @Test
    @DisplayName("测试注销登录")
    public void testLogout() throws Exception {
        ResultActions perform = mockMvc.perform(post("/user/logout").header("user_token", token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).logout(anyString());
    }
    @Test
    @DisplayName("测试登录")
    public void testLogin() throws Exception {
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail(email);
        request.setPassword("123456");
        ResultActions perform = mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(JSON.toJSONString(request)));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).login(any());
    }
    @Test
    @DisplayName("测试修改昵称")
    public void testModifyNickname() throws Exception {
        ResultActions perform = mockMvc.perform(post("/user/modifyNickname").param("nickname", "mushroom").header("user_token", token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).modifyNickname(anyString(), anyString());
    }
    @Test
    @DisplayName("测试上传文件")
    public void testUploadFile() throws Exception {
        ResultActions perform = mockMvc.perform(fileUpload("/user/uploadProfile").file(new MockMultipartFile("file","file".getBytes())).header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).uploadProfile(anyString(), any());
    }
    @Test
    @DisplayName("测试修改密码")
    public void testModifyPassword() throws Exception {
        ResultActions perform = mockMvc.perform(post("/user/modifyPassword").param("password", "123456").header("user_token", token).param("code", "123456"));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).modifyPassword(anyString(), anyString(), anyString());
    }
    @Test
    @DisplayName("测试发送验证码")
    public void testSendCode() throws Exception {
        ResultActions perform = mockMvc.perform(get("/user/sendCode").param("email", email).header("user_token", token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).sendCode(anyString());
    }
    @Test
    @DisplayName("测试管理员查找所有用户")
    public void testFindAllUsers() throws Exception {
        ResultActions perform = mockMvc.perform(get("/user/findAllUsers").header("user_token", adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).findAllUsers(anyInt(), anyInt());
    }
    @Test
    @DisplayName("测试管理员查找所有被禁用的用户")
    public void testFindAllDisabledUsers() throws Exception {
        ResultActions perform = mockMvc.perform(get("/user/findAllDisabledUsers").header("user_token", adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).findAllDisabledUsers(anyInt(), anyInt());
    }
    @Test
    @DisplayName("测试管理员禁用用户")
    public void testDisableAccount() throws Exception {
        ResultActions perform = mockMvc.perform(post("/user/disableAccount").header("user_token", adminToken).param("userId", email));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).disableAccount(anyString());
    }
    @Test
    @DisplayName("测试管理员禁用用户")
    public void testEnableAccount() throws Exception {
        ResultActions perform = mockMvc.perform(post("/user/enableAccount").header("user_token", adminToken).param("userId", email));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).enableAccount(anyString());
    }
    @Test
    @DisplayName("测试管理员更新用户信誉积分")
    public void testUpdateCredit() throws Exception {
        ResultActions perform = mockMvc.perform(post("/user/updateCredit").header("user_token", adminToken).param("userId", email).param("credit","100"));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(userService).updateCredit(anyString(), anyInt());
    }
}

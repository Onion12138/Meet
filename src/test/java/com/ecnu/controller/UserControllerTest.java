//package com.ecnu.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.ecnu.domain.User;
//import com.ecnu.dto.UserLoginRequest;
//import com.ecnu.dto.UserRegisterRequest;
//import com.ecnu.service.UserService;
//import com.ecnu.utils.JwtUtil;
//import com.ecnu.vo.ResultEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//
///**
// * @author onion
// * @date 2019/12/13 -6:17 下午
// */
//@WebMvcTest(value = UserController.class)
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private UserService userService;
//
//    User user;
//    String token;
//    @BeforeEach
//    public void generateToken(){
//        user = new User();
//        user.setEmail("969023014@qq.com");
//        user.setNickname("onion");
//        user.setAdmin(false);
//        token = JwtUtil.createJwt(user);
//    }
//    @Test
//    public void testCheckExistEmail() throws Exception{
//        when(userService.checkEmail(anyString())).thenReturn(false);
//        MvcResult mvcResult = mockMvc.perform(get("/user/check").param("email", "969023014@qq.com")).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(1001, result.getCode());
//    }
//    @Test
//    public void testCheckNotExistEmail() throws Exception{
//        when(userService.checkEmail(anyString())).thenReturn(true);
//        MvcResult mvcResult = mockMvc.perform(get("/user/check").param("email", "96902301@qq.com")).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(0, result.getCode());
//    }
//    @Test
//    public void testRightRegister() throws Exception{
//        UserRegisterRequest request = new UserRegisterRequest();
//        request.setCode("123456");
//        request.setEmail("969023014@qq.com");
//        request.setNickname("Onion");
//        request.setPassword("123456");
//        String content = JSON.toJSONString(request);
//        MvcResult mvcResult = mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(0, result.getCode());
//    }
//    @Test
//    public void testRegister() throws Exception{
//        UserRegisterRequest request = new UserRegisterRequest();
//        request.setCode("123456");
//        request.setEmail("969023014@qq.com");
//        request.setNickname("Onion");
//        request.setPassword("123456");
//        String content = JSON.toJSONString(request);
//        MvcResult mvcResult = mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(0, result.getCode());
//    }
//    @Test
//    public void testLogin() throws Exception{
//        UserLoginRequest request = new UserLoginRequest();
//        request.setEmail("969023014@qq.com");
//        request.setPassword("123456");
//        String content = JSON.toJSONString(request);
//        MvcResult mvcResult = mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(result.getCode(), 0);
//        assertTrue(result.getData() instanceof Map);
//    }
//    @Test
//    public void testModifyNickname() throws Exception{
////        MvcResult mvcResult = mockMvc.perform(post("/user/modifyNickname")
////                .param("token", "token")
////                .param("nickname", "mushroom")).andReturn();
////        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
////        assertEquals(-1, result.getCode());
//         MvcResult mvcResult = mockMvc.perform(post("/user/modifyNickname")
//                .param("nickname", "mushroom").header("user_token",token)).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        System.out.println(token);
//        assertEquals(0,result.getCode());
//    }
//    // sth wrong! how to test it?
//    @Test
//    public void testUploadProfile() throws Exception {
////        when(userService.uploadProfile(anyString(), any())).thenReturn();
//        MvcResult mvcResult = mockMvc.perform(multipart("/uploadProfile").file("file", "123".getBytes())).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(0, result.getCode());
//    }
//    @Test
//    public void testModifyPassword() throws Exception {
//        MvcResult mvcResult = mockMvc.perform(post("/user/modifyPassword")
//                .param("password","654321")
//                .param("code", "969023").header("user_token", token)).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(0, result.getCode());
//    }
//    @Test
//    public void testSendCode() throws Exception{
//        MvcResult mvcResult = mockMvc.perform(get("/user/sendCode").param("email", "969023014@qq.com")).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(0, result.getCode());
//    }
//    @Test
//    public void testFindAllUsers() throws Exception{
//        MvcResult mvcResult = mockMvc.perform(get("/user/findAllUsers")).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(0, result.getCode());
//    }
//    @Test
//    public void testFindAllDisabledUsers() throws Exception {
//        MvcResult mvcResult = mockMvc.perform(get("/user/findAllDisabledUsers")).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(0, result.getCode());
//    }
//    @Test
//    public void testDisableAccount() throws Exception {
//        MvcResult mvcResult = mockMvc.perform(post("/user/disabledAccount").param("userId","969023014@qq.com").header("user_token",token)).andReturn();
//        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
//        assertEquals(0, result.getCode());
//    }
////    @PutMapping("/uploadProfile")
////    public ResultEntity uploadProfile(@RequestParam String token, @RequestParam MultipartFile file){
////        Claims claims = JwtUtil.parseJwt(token);
////        String id = claims.getId();
////        userService.uploadProfile(id, file);
////        return ResultEntity.succeed();
////    }
////
////    @DeleteMapping("/disableAccount")
////    @AdminOnly
////    public ResultEntity disableAccount(@RequestParam String userId){
////        userService.disableAccount(userId);
////        return ResultEntity.succeed();
////    }
////
////    @PutMapping("/enableAccount")
////    @AdminOnly
////    public ResultEntity enableAccount(@RequestParam String userId){
////        userService.enableAccount(userId);
////        return ResultEntity.succeed();
////    }
////
////    @PutMapping("/updateCredit")
////    @AdminOnly
////    public ResultEntity updateCredit(@RequestParam String userId, @RequestParam Integer credit){
////        userService.updateCredit(userId, credit);
////        return ResultEntity.succeed();
////    }
//
//}

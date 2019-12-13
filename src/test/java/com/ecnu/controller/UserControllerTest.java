package com.ecnu.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.dto.UserLoginRequest;
import com.ecnu.dto.UserRegisterRequest;
import com.ecnu.service.UserService;
import com.ecnu.utils.JwtUtil;
import com.ecnu.vo.ResultEntity;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * @author onion
 * @date 2019/12/13 -6:17 下午
 */
@WebMvcTest(value = UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Test
    public void testCheckExistEmail() throws Exception{
        when(userService.checkEmail(anyString())).thenReturn(false);
        MvcResult mvcResult = mockMvc.perform(get("/user/check").param("email", "969023014@qq.com")).andReturn();
        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
        assertEquals(result.getCode(), 1001);
    }
    @Test
    public void testCheckNotExistEmail() throws Exception{
        when(userService.checkEmail(anyString())).thenReturn(true);
        MvcResult mvcResult = mockMvc.perform(get("/user/check").param("email", "96902301@qq.com")).andReturn();
        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
        assertEquals(result.getCode(), 0);
    }
    @Test
    public void testRightRegister() throws Exception{
        UserRegisterRequest request = new UserRegisterRequest();
        request.setCode("123456");
        request.setEmail("969023014@qq.com");
        request.setNickname("Onion");
        request.setPassword("123456");
        String content = JSON.toJSONString(request);
        MvcResult mvcResult = mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
        assertEquals(result.getCode(), 0);
    }
    @Test
    public void testRegister() throws Exception{
        UserRegisterRequest request = new UserRegisterRequest();
        request.setCode("123456");
        request.setEmail("969023014@qq.com");
        request.setNickname("Onion");
        request.setPassword("123456");
        String content = JSON.toJSONString(request);
        MvcResult mvcResult = mockMvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
        assertEquals(result.getCode(), 0);
    }
    @Test
    public void testLogin() throws Exception{
        UserLoginRequest request = new UserLoginRequest();
        request.setEmail("969023014@qq.com");
        request.setPassword("123456");
        String content = JSON.toJSONString(request);
        MvcResult mvcResult = mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(content)).andReturn();
        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
        assertEquals(result.getCode(), 0);
        assertTrue(result.getData() instanceof Map);
    }
    //修改
    @Test
    public void testModifyNickname() throws Exception{
        when(JwtUtil.parseJwt(anyString())).thenReturn(any(Claims.class));
        MvcResult mvcResult = mockMvc.perform(put("/user/modifyNickname")
                .param("token", "token")
                .param("nickname", "mushroom")).andReturn();
        ResultEntity result = JSON.parseObject(mvcResult.getResponse().getContentAsByteArray(), ResultEntity.class);
        assertEquals(result.getCode(), 0);
    }
//    @PutMapping("/modifyNickname")
//    public ResultEntity modifyNickname(@RequestParam String token, @RequestParam String nickname){
//        Claims claims = JwtUtil.parseJwt(token);
//        String id = claims.getId();
//        userService.modifyNickname(id, nickname);
//        return ResultEntity.succeed();
//    }
//    @PutMapping("/uploadProfile")
//    public ResultEntity uploadProfile(@RequestParam String token, @RequestParam MultipartFile file){
//        Claims claims = JwtUtil.parseJwt(token);
//        String id = claims.getId();
//        userService.uploadProfile(id, file);
//        return ResultEntity.succeed();
//    }
//    @PutMapping("/modifyPassword")
//    public ResultEntity modifyPassword(@RequestParam String token, @RequestParam String password, @RequestParam String code){
//        Claims claims = JwtUtil.parseJwt(token);
//        String id = claims.getId();
//        userService.modifyPassword(id, password, code);
//        return ResultEntity.succeed();
//    }
//    @GetMapping("/sendCode")
//    @LoginRequired(value = false)
//    public ResultEntity sendCode(@RequestParam String email){
//        userService.sendCode(email);
//        return ResultEntity.succeed();
//    }
//    @GetMapping("findAllUsers")
//    @AdminOnly
//    public ResultEntity findAllUsers(@RequestParam Integer page, @RequestParam Integer size){
//        PageInfo<User> list = userService.findAllUsers(page, size);
//        return ResultEntity.succeed(list);
//    }
//    //更多个性化搜索
//    @GetMapping("findAllDisabledUsers")
//    @AdminOnly
//    public ResultEntity findAllDisabledUsers(@RequestParam Integer page, @RequestParam Integer size){
//        PageInfo<User> list = userService.findAllDisabledUsers(page, size);
//        return ResultEntity.succeed(list);
//    }
//
//    @DeleteMapping("/disableAccount")
//    @AdminOnly
//    public ResultEntity disableAccount(@RequestParam String userId){
//        userService.disableAccount(userId);
//        return ResultEntity.succeed();
//    }
//
//    @PutMapping("/enableAccount")
//    @AdminOnly
//    public ResultEntity enableAccount(@RequestParam String userId){
//        userService.enableAccount(userId);
//        return ResultEntity.succeed();
//    }
//
//    @PutMapping("/updateCredit")
//    @AdminOnly
//    public ResultEntity updateCredit(@RequestParam String userId, @RequestParam Integer credit){
//        userService.updateCredit(userId, credit);
//        return ResultEntity.succeed();
//    }

}
package com.ecnu.controller;

import com.ecnu.domain.User;
import com.ecnu.enums.ResultEnum;
import com.ecnu.service.UserService;
import com.ecnu.utils.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    private String email;
    private String newEmail;
    private String token;
    private String adminToken;
    @Before
    public void before() {
        email = "969023014";
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

    }
//    @GetMapping("/myOrders")
//    public ResultEntity findMyOrders(){
//        String email = getEmail();
//        Set<Order> orderList = userService.findMyOrders(email);
//        return ResultEntity.succeed(orderList);
//    }
//    @PostMapping("/register")
//    @LoginRequired(value = false)
//    public ResultEntity register(@Validated @RequestBody UserRegisterRequest request, BindingResult result){
//        ParamUtil.verifyParam(result);
//        userService.register(request);
//        return ResultEntity.succeed();
//    }
//    @PostMapping("/logout")
//    public ResultEntity logout() {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String token = request.getHeader("user_token");
//        userService.logout(token);
//        return ResultEntity.succeed();
//    }
//    @PostMapping("/login")
//    @LoginRequired(value = false)
//    public ResultEntity login(@RequestBody @Valid UserLoginRequest request, BindingResult result){
//        ParamUtil.verifyParam(result);
//        Map<String, String> map = userService.login(request);
//        return ResultEntity.succeed(map);
//    }
//    @PostMapping("/modifyNickname")
//    public ResultEntity modifyNickname(@RequestParam String nickname){
//        userService.modifyNickname(getEmail(), nickname);
//        return ResultEntity.succeed();
//    }
//    @PostMapping("/uploadProfile")
//    public ResultEntity uploadProfile(@RequestParam MultipartFile file){
//        String url = userService.uploadProfile(getEmail(), file);
//        return ResultEntity.succeed(url);
//    }
//    @PostMapping("/modifyPassword")
//    public ResultEntity modifyPassword(@RequestParam String password, @RequestParam String code){
//        userService.modifyPassword(getEmail(), password, code);
//        return ResultEntity.succeed();
//    }
//    @GetMapping("/sendCode")
//    @LoginRequired(value = false)
//    public ResultEntity sendCode(@RequestParam String email){
//        userService.sendCode(email);
//        return ResultEntity.succeed();
//    }
//    @GetMapping("/findAllUsers")
//    @AdminOnly
//    public ResultEntity findAllUsers(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
//        PageInfo<User> list = userService.findAllUsers(page, size);
//        return ResultEntity.succeed(list);
//    }
//    //更多个性化搜索
//    @GetMapping("/findAllDisabledUsers")
//    @AdminOnly
//    public ResultEntity findAllDisabledUsers(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
//        PageInfo<User> list = userService.findAllDisabledUsers(page, size);
//        return ResultEntity.succeed(list);
//    }
//
//    @PostMapping("/disableAccount")
//    @AdminOnly
//    public ResultEntity disableAccount(@RequestParam String userId){
//        userService.disableAccount(userId);
//        return ResultEntity.succeed();
//    }
//
//    @PostMapping("/enableAccount")
//    @AdminOnly
//    public ResultEntity enableAccount(@RequestParam String userId){
//        userService.enableAccount(userId);
//        return ResultEntity.succeed();
//    }
//
//    @PostMapping("/updateCredit")
//    @AdminOnly
//    public ResultEntity updateCredit(@RequestParam String userId, @RequestParam Integer credit){
//        userService.updateCredit(userId, credit);
//        return ResultEntity.succeed();
//    }
}

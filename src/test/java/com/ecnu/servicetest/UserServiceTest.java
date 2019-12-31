package com.ecnu.servicetest;

import com.ecnu.daotest.UserDao;
import com.ecnu.daotest.UserMapper;
import com.ecnu.domain.User;
import com.ecnu.request.UserLoginRequest;
import com.ecnu.request.UserRegisterRequest;
import com.ecnu.enums.ResultEnum;
import com.ecnu.exception.MyException;
import com.ecnu.servicetest.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 * @author onion
 * @date 2019/12/28 -11:30 下午
 */
@SpringBootTest
public class UserServiceTest {
    @Spy
    private BCryptPasswordEncoder encoder;

    @Mock
    private MailService mailService;
    @Mock
    private UserDao userDao;
    @Mock
    private StringRedisTemplate redisTemplate;
    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterRequest registerRequest;
    private UserLoginRequest loginRequest;
    private User user;
    @BeforeEach
    public void before() {
        user = new User();
        user.setEmail("969023014@qq.com");
        user.setNickname("Onion");
        user.setAdmin(false);
        user.setDisabled(false);
        user.setCredit(10);
        user.setProfile("https://avatars2.githubusercontent.com/u/33611404?s=400&v=4");
        user.setPassword("$2a$10$Sw6Bte04ZLYD1TcPjflksO5guO3VGIPg0y/hk3xl8YP6dTrkBbn2e");
        registerRequest = new UserRegisterRequest();
        registerRequest.setEmail("969023014@qq.com");
        registerRequest.setNickname("Onion");
        registerRequest.setPassword("123456");
        registerRequest.setCode("123456");
        loginRequest = new UserLoginRequest();
        loginRequest.setPassword("123456");
        loginRequest.setEmail("969023014@qq.com");
    }
    @Test
    @DisplayName("测试邮箱是否被注册")
    public void testEmail() {
        when(userMapper.selectOne(any())).thenReturn(user);
        User u = userMapper.selectOne(this.user);
        assertNotNull(u);
        verify(userMapper).selectOne(any());
    }
    @Test
    @DisplayName("测试用户注册未获取验证码或者验证码过期，抛出验证码不存在的异常")
    public void testInvalidCodeRegister() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(null);
        assertThrows(MyException.class, () -> userService.register(registerRequest), ResultEnum.CODE_NOT_EXIST.getMessage());
    }
    @Test
    @DisplayName("测试用户注册输入错误的验证码，抛出验证码错误的异常")
    public void testWrongCodeRegister() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn("123457");
        assertThrows(MyException.class, () -> userService.register(registerRequest), ResultEnum.WRONG_CODE.getMessage());
    }
    @Test
    @DisplayName("测试用户正确输入信息后完成注册，并且密码被加密，拥有默认头像")
    public void testSuccessfulRegister() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn("123456");
        userService.register(registerRequest);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).insert(userArgumentCaptor.capture());
        assertAll(() -> assertEquals(user.getEmail(), userArgumentCaptor.getValue().getEmail()),
                () -> assertEquals(user.getNickname(), userArgumentCaptor.getValue().getNickname()),
                () -> assertTrue(encoder.matches("123456", userArgumentCaptor.getValue().getPassword())),
                () -> assertEquals(user.getAdmin(), userArgumentCaptor.getValue().getAdmin()),
                () -> assertEquals(user.getDisabled(), userArgumentCaptor.getValue().getDisabled()),
                () -> assertEquals(user.getCredit(), userArgumentCaptor.getValue().getCredit()),
                () -> assertEquals(user.getProfile(), userArgumentCaptor.getValue().getProfile())
        );
    }
    @Test
    @DisplayName("测试用户注销")
    public void testLogout() {
        userService.logout(anyString());
        verify(redisTemplate).delete(anyString());
    }
    @Test
    @DisplayName("测试未注册的用户登陆，会抛出用户不存在的异常")
    public void testNotRegisterUserLogin() {
        when(userMapper.selectOne(any())).thenReturn(null);
        assertThrows(MyException.class, () -> userService.login(loginRequest), ResultEnum.USER_NOT_EXIST.getMessage());
    }
    @Test
    @DisplayName("测试输入错误密码的用户登陆会抛出密码错误的异常")
    public void testWrongPasswordLogin() {
        when(userMapper.selectOne(any())).thenReturn(user);
        loginRequest.setPassword("123457");
        assertThrows(MyException.class, () -> userService.login(loginRequest),ResultEnum.WRONG_PASSWORD.getMessage());
    }
    @Test
    @DisplayName("测试用户被禁用后登陆会抛出异常")
    public void testDisabledLogin() {
        user.setDisabled(true);
        when(userMapper.selectOne(any())).thenReturn(user);
        assertThrows(MyException.class, () -> userService.login(loginRequest),ResultEnum.ACCOUNT_DISABLED.getMessage());
    }
    @Test
    @DisplayName("测试用户正确登陆后返回map信息")
    public void testSuccessLogin() {
        when(userMapper.selectOne(any())).thenReturn(user);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Map<String, String> info = userService.login(loginRequest);
        assertAll(() -> assertFalse(info.get("token").isEmpty()),
                () -> assertEquals(user.getNickname(), info.get("nickname")),
                () -> assertEquals(user.getProfile(), info.get("profile")),
                () -> assertEquals(user.getEmail(), info.get("email")),
                () -> assertEquals("user", info.get("role"))
        );
    }
    @Test
    @DisplayName("测试用户修改昵称")
    public void testModifyNickname() {
        userService.modifyNickname(user.getEmail(), "mushroom");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).updateByPrimaryKeySelective(captor.capture());
        assertEquals("mushroom",captor.getValue().getNickname());
    }
//    @Test
//    @DisplayName("测试用户上传文件")
//    public void testUploadProfile() {
//        userService.uploadProfile(user.getEmail(), new MockMultipartFile("mock", "test".getBytes()));
//        verify(userMapper).updateByPrimaryKeySelective(any());
//    }
    @Test
    @DisplayName("测试用户未发送验证码修改密码，会抛出验证码不存在的异常")
    public void testModifyPasswordWithoutCode() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn(null);
        assertThrows(MyException.class, () -> userService.modifyPassword(user.getEmail(), "123456","123456"),ResultEnum.CODE_NOT_EXIST.getMessage());
    }
    @Test
    @DisplayName("测试用户输入错误验证码修改密码，会抛出验证码错误异常")
    public void testModifyPasswordWithWrongCode() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn("123457");
        assertThrows(MyException.class, () -> userService.modifyPassword(user.getEmail(), "123456","123456"),ResultEnum.WRONG_CODE.getMessage());
    }
    @Test
    @DisplayName("测试用户正确修改密码")
    public void testSuccessfulModifyPassword() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(any())).thenReturn("123456");
        userService.modifyPassword(user.getEmail(), "999999","123456");
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).updateByPrimaryKeySelective(captor.capture());
        assertTrue(encoder.matches("999999",captor.getValue().getPassword()));
    }
    @Test
    @DisplayName("测试发送邮件")
    public void testSendCode() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        userService.sendCode(user.getEmail());
        verify(mailService).sendMail(anyString(), any(), anyString());
    }
    @Test
    @DisplayName("测试查看所有用户")
    public void testFindAllUsers() {
        userService.findAllUsers(1, 5);
        verify(userMapper).selectAll();
    }
    @Test
    @DisplayName("测试禁用用户")
    public void testDisableAccount() {
        userService.disableAccount(user.getEmail());
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).updateByPrimaryKeySelective(captor.capture());
        assertEquals(user.getEmail(), captor.getValue().getEmail());
        assertTrue(captor.getValue().getDisabled());
    }
    @Test
    @DisplayName("测试启用用户")
    public void testEnableAccount() {
        userService.enableAccount(user.getEmail());
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).updateByPrimaryKeySelective(captor.capture());
        assertEquals(user.getEmail(), captor.getValue().getEmail());
        assertFalse(captor.getValue().getDisabled());
    }
    @Test
    @DisplayName("测试查找所有被禁用的用户")
    public void testFindAllDisabledUsers() {
        userService.findAllDisabledUsers(1,5);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).select(captor.capture());
        assertTrue(captor.getValue().getDisabled());
    }
    @Test
    @DisplayName("测试更新信誉积分")
    public void testUpdateCredit() {
        userService.updateCredit(user.getEmail(), 100);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userMapper).updateByPrimaryKeySelective(captor.capture());
        assertEquals(100, captor.getValue().getCredit());
        assertEquals(user.getEmail(), captor.getValue().getEmail());
    }
    @Test
    @DisplayName("查看某个用户的订单")
    public void testFindUserOrders() {
        userService.findUserOrders(user.getEmail());
        verify(userDao).findById(anyString());
    }
}

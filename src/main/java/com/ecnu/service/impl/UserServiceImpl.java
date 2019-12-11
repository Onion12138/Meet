package com.ecnu.service.impl;

import com.ecnu.dao.UserDao;
import com.ecnu.domain.User;
import com.ecnu.dto.UserLoginRequest;
import com.ecnu.dto.UserRegisterRequest;
import com.ecnu.enums.ResultEnum;
import com.ecnu.exception.MyException;
import com.ecnu.service.MailService;
import com.ecnu.service.UserService;
import com.ecnu.utils.CodeUtil;
import com.ecnu.utils.JwtUtil;
import com.ecnu.utils.KeyUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author onion
 * @date 2019/12/11 -6:23 下午
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MailService mailService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Value("600")
    private int expireTime;
    @Value("This is the code from notehub")
    private String subject;
    @Override
    public boolean checkEmail(String email) {
        User user = userDao.findByEmail(email);
        return user != null;
    }

    @Override
    public void register(UserRegisterRequest request) {
        User user = new User();
        String code = redisTemplate.opsForValue().get("code_" + request.getEmail());
        if(code == null){
            throw new MyException(ResultEnum.CODE_NOT_EXIST);
        }
        if(!code.equals(request.getCode())){
            throw new MyException(ResultEnum.WRONG_CODE);
        }
        user.setCredit(10);
        user.setAdmin(false);
        user.setDisabled(false);
        user.setId(KeyUtil.genUniqueKey());
        user.setNickname(request.getNickname());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRegisterTime(LocalDateTime.now());
        user.setEmail(request.getEmail());
        user.setProfileUrl("https://avatars2.githubusercontent.com/u/33611404?s=400&v=4");
        userDao.save(user);
    }

    @Override
    public Map<String, String> login(UserLoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        User user = userDao.findByEmail(email);
        if (user == null){
            throw new MyException(ResultEnum.USER_NOT_EXIST);
        }
        if (!encoder.matches(password, user.getPassword())){
            throw new MyException(ResultEnum.WRONG_PASSWORD);
        }
        Map <String, String> map = new HashMap<>();
        String token = JwtUtil.createJwt(user);
        String nickname = user.getNickname();
        map.put("token", token);
        map.put("nickname", nickname);
        map.put("profile",user.getProfileUrl());
        map.put("role",user.isAdmin() ? "admin" : "user");
        return map;
    }

    @Override
    public void modifyNickname(String id, String nickname) {
        userDao.modifyNickname(nickname);
    }

    @Override
    public void uploadProfile(String id, MultipartFile file) {

    }

    @Override
    public void modifyPassword(String id, String password) {

    }

    @Override
    public void sendCode(String email) {
        String code = CodeUtil.getCode();
        redisTemplate.opsForValue().set("code_"+email, code, expireTime, TimeUnit.SECONDS);
        String content = "your code is " + code + " , please complete your registration in 10 minutes.";
        mailService.sendMail(email,subject,content);
    }

    @Override
    public PageInfo<User> findAllUsers(int page, int size) {
        return null;
    }

    @Override
    public void disableAccount(String userId) {

    }

}

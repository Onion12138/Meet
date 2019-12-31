package com.ecnu.service.impl;

import com.ecnu.dao.UserDao;
import com.ecnu.dao.UserMapper;
import com.ecnu.domain.Order;
import com.ecnu.domain.User;
import com.ecnu.request.UserLoginRequest;
import com.ecnu.request.UserRegisterRequest;
import com.ecnu.enums.ResultEnum;
import com.ecnu.exception.MyException;
import com.ecnu.service.MailService;
import com.ecnu.service.UserService;
import com.ecnu.utils.CodeUtil;
import com.ecnu.utils.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author onion
 * @date 2019/12/11 -6:23 下午
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
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
    @Value("This is the code from meetHere")
    private String subject;
    @Value("${qiniu.access-key}")
    private String accessKey;
    @Value("${qiniu.secret-key}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("2592000")
    private long expireInSeconds;
    @Override
    public boolean checkEmail(String email) {
        User u = new User();
        u.setEmail(email);
        User user = userMapper.selectOne(u);
        return user == null;
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
        user.setNickname(request.getNickname());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRegisterTime(LocalDateTime.now());
        user.setEmail(request.getEmail());
        user.setProfile("https://avatars2.githubusercontent.com/u/33611404?s=400&v=4");
        userMapper.insert(user);
    }

    @Override
    public void logout(String token) {
        redisTemplate.delete(token);
    }

    @Override
    public Map<String, String> login(UserLoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        User u = new User();
        u.setEmail(email);
        User user = userMapper.selectOne(u);
        if (user == null){
            throw new MyException(ResultEnum.USER_NOT_EXIST);
        }
        if (!encoder.matches(password, user.getPassword())){
            throw new MyException(ResultEnum.WRONG_PASSWORD);
        }
        if (user.getDisabled()){
            throw new MyException(ResultEnum.ACCOUNT_DISABLED);
        }
        Map <String, String> map = new HashMap<>();
        String token = JwtUtil.createJwt(user);
        redisTemplate.opsForValue().set(token, user.getEmail());
        String nickname = user.getNickname();
        map.put("token", token);
        map.put("nickname", nickname);
        map.put("profile",user.getProfile());
        map.put("email",user.getEmail());
        map.put("role",user.getAdmin() ? "admin" : "user");
        return map;
    }

    @Override
    public void modifyNickname(String email, String nickname) {
        User user = new User();
        user.setEmail(email);
        user.setNickname(nickname);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public String uploadProfile(String email, MultipartFile file){
        String name = file.getOriginalFilename();
        InputStream fileInputStream = null;
        try {
            fileInputStream = file.getInputStream();
        } catch (IOException e) {
            throw new MyException(e.getMessage(), -1);
        }
        String key = email + name;
        Configuration cfg = new Configuration(Region.region2());
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(fileInputStream, key, upToken, null, null);
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            log.info("upload file : {}", putRet);
        } catch (QiniuException ex) {
            throw new MyException(ResultEnum.FILE_UPLOAD_ERROR);
        }
        User user = new User();
        user.setEmail(email);
        String url = getProfileUrl(key);
        user.setProfile(url);
        userMapper.updateByPrimaryKeySelective(user);
        return url;
    }

    private String getProfileUrl(String filename){
        String domainOfBucket = "http://ecnuonion.club";
        String encodedFileName = null;
        try {
            encodedFileName = URLEncoder.encode(filename, "utf-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new MyException(e.getMessage(), -1);
        }
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        Auth auth = Auth.create(accessKey, secretKey);
        return auth.privateDownloadUrl(publicUrl, expireInSeconds);
    }

    @Override
    public void modifyPassword(String email, String password, String code) {
        String passwordCode = redisTemplate.opsForValue().get("code_" + email);
        if(passwordCode == null){
            throw new MyException(ResultEnum.CODE_NOT_EXIST);
        }
        if(!passwordCode.equals(code)){
            throw new MyException(ResultEnum.WRONG_CODE);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void sendCode(String email) {
        String code = CodeUtil.getCode();
        redisTemplate.opsForValue().set("code_" + email, code, expireTime, TimeUnit.SECONDS);
        String content = "your code is " + code + " , please complete your registration in 10 minutes.";
        mailService.sendMail(email,subject,content);
    }

    @Override
    public PageInfo<User> findAllUsers(int page, int size) {
        PageHelper.startPage(page, size);
        List<User> userList = userMapper.selectAll();
        return new PageInfo<>(userList);
    }

    @Override
    public void disableAccount(String email) {
        User user = new User();
        user.setEmail(email);
        user.setDisabled(true);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void enableAccount(String email) {
        User user = new User();
        user.setEmail(email);
        user.setDisabled(false);
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public PageInfo<User> findAllDisabledUsers(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        User user = new User();
        user.setDisabled(true);
        List<User> users = userMapper.select(user);
        return new PageInfo<>(users);
    }

    @Override
    public Set<Order> findUserOrders(String email) {
        Optional<User> optional = userDao.findById(email);
        return optional.map(User::getOrderSet).orElse(null);
    }



    @Override
    public void updateCredit(String email, Integer credit) {
        User user = userDao.findById(email).get();
        user.setEmail(email);
        user.setCredit(credit);
        userDao.save(user);
//        userMapper.updateByPrimaryKeySelective(user);
    }



}

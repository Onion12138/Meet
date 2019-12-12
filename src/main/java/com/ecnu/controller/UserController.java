package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.annotation.LoginRequired;
import com.ecnu.domain.User;
import com.ecnu.dto.UserLoginRequest;
import com.ecnu.dto.UserRegisterRequest;
import com.ecnu.enums.ResultEnum;
import com.ecnu.service.UserService;
import com.ecnu.utils.JwtUtil;
import com.ecnu.vo.ResultEntity;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author onion
 * @date 2019/12/10 -11:02 下午
 * function:
 * 1. Get：邮箱是否被注册/发送验证码
 * 2. Post：注册/登陆
 * 3. Put：修改昵称/头像/密码
 * 4. Delete：禁用账户
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("check")
    @LoginRequired(value = false)
    public ResultEntity checkEmail(@RequestParam String email){
        if (userService.checkEmail(email)){
            return ResultEntity.succeed();
        }
        else{
            return ResultEntity.fail(ResultEnum.EMAIL_IN_USE);
        }
    }
    @PostMapping("/register")
    @LoginRequired(value = false)
    public ResultEntity register(@Validated @RequestBody UserRegisterRequest request, BindingResult result){
        userService.register(request);
        return ResultEntity.succeed();
    }
    @PostMapping("/login")
    @LoginRequired(value = false)
    public ResultEntity login(@Validated @RequestBody UserLoginRequest request, BindingResult result){
        Map<String, String> map = userService.login(request);
        return ResultEntity.succeed(map);
    }
    @PutMapping("/modifyNickname")
    public ResultEntity modifyNickname(@RequestParam String token, @RequestParam String nickname){
        Claims claims = JwtUtil.parseJwt(token);
        String id = claims.getId();
        userService.modifyNickname(id, nickname);
        return ResultEntity.succeed();
    }
    @PutMapping("/uploadProfile")
    public ResultEntity uploadProfile(@RequestParam String token, @RequestParam MultipartFile file){
        Claims claims = JwtUtil.parseJwt(token);
        String id = claims.getId();
        userService.uploadProfile(id, file);
        return ResultEntity.succeed();
    }
    @PutMapping("/modifyPassword")
    public ResultEntity modifyPassword(@RequestParam String token, @RequestParam String password, @RequestParam String code){
        Claims claims = JwtUtil.parseJwt(token);
        String id = claims.getId();
        userService.modifyPassword(id, password, code);
        return ResultEntity.succeed();
    }
    @GetMapping("/sendCode")
    @LoginRequired(value = false)
    public ResultEntity sendCode(@RequestParam String email){
        userService.sendCode(email);
        return ResultEntity.succeed();
    }
    @GetMapping("findAllUsers")
    @AdminOnly
    public ResultEntity findAllUsers(@RequestParam Integer page, @RequestParam Integer size){
        PageInfo<User> list = userService.findAllUsers(page, size);
        return ResultEntity.succeed(list);
    }
    //更多个性化搜索
    @GetMapping("findAllDisabledUsers")
    @AdminOnly
    public ResultEntity findAllDisabledUsers(@RequestParam Integer page, @RequestParam Integer size){
        PageInfo<User> list = userService.findAllDisabledUsers(page, size);
        return ResultEntity.succeed(list);
    }

    @DeleteMapping("/disableAccount")
    @AdminOnly
    public ResultEntity disableAccount(@RequestParam String userId){
        userService.disableAccount(userId);
        return ResultEntity.succeed();
    }

    @PutMapping("/enableAccount")
    @AdminOnly
    public ResultEntity enableAccount(@RequestParam String userId){
        userService.enableAccount(userId);
        return ResultEntity.succeed();
    }

    @PutMapping("/updateCredit")
    @AdminOnly
    public ResultEntity updateCredit(@RequestParam String userId, @RequestParam Integer credit){
        userService.updateCredit(userId, credit);
        return ResultEntity.succeed();
    }

}

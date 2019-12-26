package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.annotation.LoginRequired;
import com.ecnu.domain.Order;
import com.ecnu.domain.User;
import com.ecnu.dto.UserLoginRequest;
import com.ecnu.dto.UserRegisterRequest;
import com.ecnu.enums.ResultEnum;
import com.ecnu.service.UserService;
import com.ecnu.utils.JwtUtil;
import com.ecnu.utils.ParamUtil;
import com.ecnu.vo.ResultEntity;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/10 -11:02 下午
 * function:
 * 1. Get：邮箱是否被注册/发送验证码
 * 2. Post：注册/登陆
 * 3. Put：修改昵称/头像/密码
 * 4. Delete：禁用账户
 * 这个controller的文档参考群里的pdf
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/check")
    @LoginRequired(value = false)
    public ResultEntity checkEmail(@RequestParam String email){
        if (userService.checkEmail(email)){
            return ResultEntity.succeed();
        }
        else{
            return ResultEntity.fail(ResultEnum.EMAIL_IN_USE);
        }
    }
    @GetMapping("/myOrders")
    public ResultEntity findMyOrders(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
        Claims claims = JwtUtil.parseJwt(token);
        String id = claims.getId();
        Set<Order> orderList = userService.findMyOrders(id);
        return ResultEntity.succeed(orderList);
    }
    @PostMapping("/register")
    @LoginRequired(value = false)
    public ResultEntity register(@Validated @RequestBody UserRegisterRequest request, BindingResult result){
        ParamUtil.verifyParam(result);
        userService.register(request);
        return ResultEntity.succeed();
    }
    @PostMapping("/login")
    @LoginRequired(value = false)
    public ResultEntity login(@RequestBody @Valid UserLoginRequest request, BindingResult result){
        ParamUtil.verifyParam(result);
        Map<String, String> map = userService.login(request);
        return ResultEntity.succeed(map);
    }
    @PostMapping("/modifyNickname")
    public ResultEntity modifyNickname(@RequestParam String nickname){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
        Claims claims = JwtUtil.parseJwt(token);
        String id = claims.getId();
        userService.modifyNickname(id, nickname);
        return ResultEntity.succeed();
    }
    @PostMapping("/uploadProfile")
    public ResultEntity uploadProfile(@RequestParam MultipartFile file){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
        Claims claims = JwtUtil.parseJwt(token);
        String id = claims.getId();
        String url = userService.uploadProfile(id, file);
        return ResultEntity.succeed(url);
    }
    @PostMapping("/modifyPassword")
    public ResultEntity modifyPassword(@RequestParam String password, @RequestParam String code){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
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
    @GetMapping("/findAllUsers")
    @AdminOnly
    public ResultEntity findAllUsers(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        PageInfo<User> list = userService.findAllUsers(page, size);
        return ResultEntity.succeed(list);
    }
    //更多个性化搜索
    @GetMapping("/findAllDisabledUsers")
    @AdminOnly
    public ResultEntity findAllDisabledUsers(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        PageInfo<User> list = userService.findAllDisabledUsers(page, size);
        return ResultEntity.succeed(list);
    }

    @PostMapping("/disableAccount")
    @AdminOnly
    public ResultEntity disableAccount(@RequestParam String userId){
        userService.disableAccount(userId);
        return ResultEntity.succeed();
    }

    @PostMapping("/enableAccount")
    @AdminOnly
    public ResultEntity enableAccount(@RequestParam String userId){
        userService.enableAccount(userId);
        return ResultEntity.succeed();
    }

    @PostMapping("/updateCredit")
    @AdminOnly
    public ResultEntity updateCredit(@RequestParam String userId, @RequestParam Integer credit){
        userService.updateCredit(userId, credit);
        return ResultEntity.succeed();
    }

}

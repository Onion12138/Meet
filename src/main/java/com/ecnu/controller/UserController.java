package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.annotation.LoginRequired;
import com.ecnu.domain.Order;
import com.ecnu.domain.User;
import com.ecnu.request.UserLoginRequest;
import com.ecnu.request.UserRegisterRequest;
import com.ecnu.enums.ResultEnum;
import com.ecnu.service.UserService;
import com.ecnu.utils.JwtUtil;
import com.ecnu.utils.ParamUtil;
import com.ecnu.vo.ResultVO;
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
import java.util.Objects;
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
    private String getEmail() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
        Claims claims = JwtUtil.parseJwt(token);
        return claims.getId();
    }
    @GetMapping("/check")
    @LoginRequired(value = false)
    public ResultVO checkEmail(@RequestParam String email){
        if (userService.checkEmail(email)){
            return ResultVO.succeed();
        }
        else{
            return ResultVO.fail(ResultEnum.EMAIL_IN_USE);
        }
    }
    /*
    * 只提供给管理员使用，用户查看自己的订单在OrderController里面有接口。
    * 场景：管理员先是查看所有的用户，点击具体用户后可以查看其订单情况。
    * */
    @AdminOnly
    @GetMapping("/UserOrders")
    public ResultVO findMyOrders(@RequestParam String email){
        Set<Order> orderList = userService.findUserOrders(email);
        return ResultVO.succeed(orderList);
    }
    @PostMapping("/register")
    @LoginRequired(value = false)
    public ResultVO register(@Validated @RequestBody UserRegisterRequest request, BindingResult result){
        ParamUtil.verifyParam(result);
        userService.register(request);
        return ResultVO.succeed();
    }
    @PostMapping("/logout")
    public ResultVO logout() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String token = request.getHeader("user_token");
        userService.logout(token);
        return ResultVO.succeed();
    }
    @PostMapping("/login")
    @LoginRequired(value = false)
    public ResultVO login(@RequestBody @Valid UserLoginRequest request, BindingResult result){
        ParamUtil.verifyParam(result);
        Map<String, String> map = userService.login(request);
        return ResultVO.succeed(map);
    }
    @PostMapping("/modifyNickname")
    public ResultVO modifyNickname(@RequestParam String nickname){
        userService.modifyNickname(getEmail(), nickname);
        return ResultVO.succeed();
    }
    @PostMapping("/uploadProfile")
    public ResultVO uploadProfile(@RequestParam MultipartFile file){
        String url = userService.uploadProfile(getEmail(), file);
        return ResultVO.succeed(url);
    }
    @PostMapping("/modifyPassword")
    public ResultVO modifyPassword(@RequestParam String password, @RequestParam String code){
        userService.modifyPassword(getEmail(), password, code);
        return ResultVO.succeed();
    }
    @GetMapping("/sendCode")
    @LoginRequired(value = false)
    public ResultVO sendCode(@RequestParam String email){
        userService.sendCode(email);
        return ResultVO.succeed();
    }
    @GetMapping("/findAllUsers")
    @AdminOnly
    public ResultVO findAllUsers(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        PageInfo<User> list = userService.findAllUsers(page, size);
        return ResultVO.succeed(list);
    }
    //更多个性化搜索
    @GetMapping("/findAllDisabledUsers")
    @AdminOnly
    public ResultVO findAllDisabledUsers(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        PageInfo<User> list = userService.findAllDisabledUsers(page, size);
        return ResultVO.succeed(list);
    }

    @PostMapping("/disableAccount")
    @AdminOnly
//    @LoginRequired(value = false)
    public ResultVO disableAccount(@RequestParam String userId){
        userService.disableAccount(userId);
        return ResultVO.succeed();
    }

    @PostMapping("/enableAccount")
//    @AdminOnly
    @LoginRequired(value = false)
    public ResultVO enableAccount(@RequestParam String userId){
        userService.enableAccount(userId);
        return ResultVO.succeed();
    }

    @PostMapping("/updateCredit")
    @AdminOnly
    public ResultVO updateCredit(@RequestParam String userId, @RequestParam Integer credit){
        userService.updateCredit(userId, credit);
        return ResultVO.succeed();
    }

}

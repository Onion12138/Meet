package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.annotation.LoginRequired;
import com.ecnu.dto.UserRegisterRequest;
import com.ecnu.vo.ResultEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author onion
 * @date 2019/12/10 -11:02 下午
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("check")
    public ResultEntity checkEmail(@RequestParam String email){
        return ResultEntity.succeed();
    }
    @PostMapping("/register")
    public ResultEntity register(@Validated @RequestBody UserRegisterRequest request){
        return ResultEntity.succeed();
    }
    @PostMapping("/login")
    public ResultEntity login(){
        return ResultEntity.succeed();
    }

    @PutMapping("/modifyInfo")
    @LoginRequired
    public ResultEntity modifyInformation(){
        return ResultEntity.succeed();
    }
    @GetMapping("/sendCode")
    public ResultEntity sendCode(@RequestParam String email){
        return ResultEntity.succeed();
    }
    @DeleteMapping("/disableAccount")
    @LoginRequired
    @AdminOnly
    public ResultEntity disableAccount(){
        return ResultEntity.succeed();
    }
}

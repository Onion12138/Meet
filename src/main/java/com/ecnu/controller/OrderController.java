package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.annotation.LoginRequired;
import com.ecnu.vo.ResultEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author onion
 * @date 2019/12/10 -11:34 下午
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @GetMapping("/myOrder")
    @LoginRequired
    public ResultEntity findMyOrders(){
        return ResultEntity.succeed();
    }

    @GetMapping("/currentOrder")
    @LoginRequired
    @AdminOnly
    public ResultEntity findCurrentOrder(){
        return ResultEntity.succeed();
    }

    @GetMapping("/futureOrder")
    @LoginRequired
    @AdminOnly
    public ResultEntity findFutureOrder(){
        return ResultEntity.succeed();
    }

    @GetMapping("/pastOrder")
    @LoginRequired
    @AdminOnly
    public ResultEntity findPastOrder(){
        return ResultEntity.succeed();
    }

    @PostMapping("/addOrder")
    @LoginRequired
    public ResultEntity addOrder(){
        return ResultEntity.succeed();
    }

    @PutMapping("/updateOrder")
    @LoginRequired
    public ResultEntity updateOrder(){
        return ResultEntity.succeed();
    }

    @GetMapping("/gymOrder")
    @LoginRequired
    @AdminOnly
    public ResultEntity findOrderByGym(){
        return ResultEntity.succeed();
    }

    @DeleteMapping("cancelOrder")
    @LoginRequired
    public ResultEntity cancelOrder(){
        return ResultEntity.succeed();
    }

}

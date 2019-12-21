package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.annotation.LoginRequired;
import com.ecnu.annotation.VerifyParams;
import com.ecnu.domain.Order;
import com.ecnu.dto.AvailableTimeRequest;
import com.ecnu.dto.OrderCommentRequest;
import com.ecnu.dto.OrderRequest;
import com.ecnu.service.OrderService;
import com.ecnu.utils.JwtUtil;
import com.ecnu.vo.ResultEntity;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author onion
 * @date 2019/12/10 -11:34 下午
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    private String getId(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
        Claims claims = JwtUtil.parseJwt(token);
        return claims.getId();
    }
    @GetMapping("/available")
    @VerifyParams
    public ResultEntity findAvailableTime(@Validated @RequestBody AvailableTimeRequest request, BindingResult result){
        List<Integer[]> timeInterval = orderService.findAvailableTime(request);
        return ResultEntity.succeed(timeInterval);
    }
    @GetMapping("/myOrder")
    public ResultEntity findMyOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        String id = getId();
        PageInfo<Order> orderPageInfo = orderService.findOrdersByUserId(id, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/myCurrentOrder")
    public ResultEntity findMyCurrentOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        String id = getId();
        PageInfo<Order> orderPageInfo = orderService.findMyCurrentOrders(id, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/myFutureOrder")
    public ResultEntity findMyFutureOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        String id = getId();
        PageInfo<Order> orderPageInfo = orderService.findMyFutureOrders(id, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/myPastOrder")
    public ResultEntity findMyPastOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size) {
        String id = getId();
        PageInfo<Order> orderPageInfo = orderService.findMyPastOrders(id, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/myOrderByGym")
    public ResultEntity findMyOrdersByGym(@RequestParam String gymId, @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "5") Integer size){
        String id = getId();
        PageInfo<Order> orderPageInfo = orderService.findMyOrdersByGym(id, gymId, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @PostMapping("/addOrder")
    @VerifyParams
    public ResultEntity addOrder(@Validated @RequestBody OrderRequest orderRequest, BindingResult result){
        String email = getId();
        orderService.addOrder(orderRequest, email);
        return ResultEntity.succeed();
    }

    @PostMapping("/comment")
    @VerifyParams
    public ResultEntity commentOrder(@Validated @RequestBody OrderCommentRequest request, BindingResult result){
        orderService.commentOrder(request.getOrderId(), request.getScore(), request.getComment());
        return ResultEntity.succeed();
    }

    @PostMapping("cancelMyOrder")
    public ResultEntity cancelOrder(@RequestParam String orderId){
        orderService.cancelOrder(orderId);
        return ResultEntity.succeed();
    }

    @GetMapping("/allOrder")
    @AdminOnly
    public ResultEntity findAllOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        PageInfo<Order> orderPageInfo = orderService.findAllOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/currentOrder")
    @AdminOnly
    public ResultEntity findAllCurrentOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        PageInfo<Order> orderPageInfo = orderService.findAllCurrentOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/futureOrder")
    @AdminOnly
    public ResultEntity findAllFutureOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        PageInfo<Order> orderPageInfo = orderService.findAllFutureOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/pastOrder")
    @AdminOnly
    public ResultEntity findAllPastOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        PageInfo<Order> orderPageInfo = orderService.findAllPastOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/gymOrder")
    @AdminOnly
    public ResultEntity findOrdersByGym(@RequestParam String gymId, @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "5") Integer size){
        PageInfo<Order> orderPageInfo = orderService.findAllOrdersByGymId(gymId, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/userOrder")
    @AdminOnly
    public ResultEntity findOrdersByUser(@RequestParam String email,@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "5") Integer size){
        PageInfo<Order> orderPageInfo = orderService.findOrdersByUserId(email, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @PostMapping("/testOnly")
    @LoginRequired(value = false)
    public ResultEntity insertOrder(@RequestBody OrderRequest request){
        orderService.testInsert(request);
        return ResultEntity.succeed();
    }
}

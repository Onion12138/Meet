package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.domain.Order;
import com.ecnu.dto.OrderRequest;
import com.ecnu.service.OrderService;
import com.ecnu.utils.JwtUtil;
import com.ecnu.vo.ResultEntity;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

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

    @GetMapping("/myGymOrder") //根据Gym类别Group by
    public ResultEntity findMyGymOrder(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        String id = getId();
        PageInfo<Order> orderPageInfo = orderService.findMyOrdersGroupByGym(id, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/myOrderByGym") //根据Gym类别查找
    public ResultEntity findMyOrdersByGym(@RequestParam String gymId,
                                          @RequestParam Integer page, @RequestParam Integer size){
        String id = getId();
        PageInfo<Order> orderPageInfo = orderService.findMyOrdersByGym(id, gymId, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @PostMapping("/addOrder")
    public ResultEntity addOrder(@Validated @RequestBody OrderRequest orderRequest){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
        Claims claims = JwtUtil.parseJwt(token);
        String email = claims.getId();
        orderService.addOrder(orderRequest, email);
        return ResultEntity.succeed();
    }

    @PostMapping("/comment")
    public ResultEntity commentOrder(@RequestParam String token, @RequestParam Integer score, @RequestParam String comment){
        Claims claims = JwtUtil.parseJwt(token);
        String userId = claims.getId();
        orderService.commentOrder(userId, score, comment);
        return ResultEntity.succeed();
    }

    @PostMapping("cancelMyOrder")
    public ResultEntity cancelOrder(@RequestParam String token, @RequestParam String orderId){
        Claims claims = JwtUtil.parseJwt(token);
        String userId = claims.getId();
        orderService.cancelOrder(userId, orderId);
        return ResultEntity.succeed();
    }

    @GetMapping("/allOrder")
    @AdminOnly
    public ResultEntity findAllOrders(@RequestParam Integer page, @RequestParam Integer size){
        PageInfo<Order> orderPageInfo = orderService.findAllOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/currentOrder")
    @AdminOnly
    public ResultEntity findAllCurrentOrders(@RequestParam Integer page, @RequestParam Integer size){
        PageInfo<Order> orderPageInfo = orderService.findAllCurrentOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/futureOrder")
    @AdminOnly
    public ResultEntity findAllFutureOrders(@RequestParam Integer page, @RequestParam Integer size){
        PageInfo<Order> orderPageInfo = orderService.findAllFutureOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/pastOrder")
    @AdminOnly
    public ResultEntity findAllPastOrders(@RequestParam Integer page, @RequestParam Integer size){
        PageInfo<Order> orderPageInfo = orderService.findAllPastOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @GetMapping("/gymOrder")
    @AdminOnly
    public ResultEntity findOrdersByGym(@RequestParam String gymId, @RequestParam Integer page, @RequestParam Integer size){
        PageInfo<Order> orderPageInfo = orderService.findAllOrdersByGymId(gymId, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    @PostMapping("/cancelOrder")
    @AdminOnly
    public ResultEntity cancelOrders(@RequestParam Set<String> orderIds){
        orderService.cancelOrders(orderIds);
        return ResultEntity.succeed();
    }

}

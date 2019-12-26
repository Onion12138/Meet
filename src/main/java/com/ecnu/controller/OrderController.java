package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.domain.Order;
import com.ecnu.dto.AvailableTimeRequest;
import com.ecnu.dto.OrderCommentRequest;
import com.ecnu.dto.OrderRequest;
import com.ecnu.service.OrderService;
import com.ecnu.utils.JwtUtil;
import com.ecnu.utils.ParamUtil;
import com.ecnu.vo.ResultEntity;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    /*
    * 查看那些时间段被占用。返回二维数组表示当前场馆被预约的时间段，例如[[16,17],[23,25]]，表示8点-8点半，11点半-12点半的场馆已经被预约。
    * 时间的规定：只能预约早上8点到晚上8点的时间。早上8点对应数字16，每半个小时数字加1，所以23对应时间为上午11点半。
    * 开始时间范围：16-39（即早上8点到晚上7点半）
    * 结束时间范围：17-40（即早上8点半到晚上8点）
    * 预约时间最少半个小时，最多2个小时，即1<=endTime-startTime<=4
    * 需要的参数封装为AvailableTimeRequest
    * 该对象两个属性，一个为预约日期date，格式为字符串yyyy/MM/dd，一个为场馆gymId。两者缺一不可
     * */
    @PostMapping("/available")
    public ResultEntity findAvailableTime(@Validated @RequestBody AvailableTimeRequest request, BindingResult result){
        ParamUtil.verifyParam(result);
        List<Integer[]> timeInterval = orderService.findAvailableTime(request);
        return ResultEntity.succeed(timeInterval);
    }
    /*
    * 查看我所有的订单，用的JPA查询，返回的Page和用PageHelper返回的PageInfo不太一样，注意！
    * 使用了join查询，还会返回场馆的信息
    * 按照时间降序排列
    * */
    @GetMapping("/myOrder")
    public ResultEntity findMyOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        String email = getId();
        Page<Order> pageInfo = orderService.findOrderByEmail(email, page, size);
        return ResultEntity.succeed(pageInfo);
    }
    /*
    * 查看我正在进行中的订单，数目很少
     */
    @GetMapping("/myCurrentOrder")
    public ResultEntity findMyCurrentOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        String id = getId();
        Page<Order> orderPageInfo = orderService.findMyCurrentOrders(id, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }
    /*
    * 查看我将来的订单，按照时间升序排列
    * */

    @GetMapping("/myFutureOrder")
    public ResultEntity findMyFutureOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        String id = getId();
        Page<Order> orderPageInfo = orderService.findMyFutureOrders(id, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    /*
    * 查看过去的订单，按照时间降序排列
    * */
    @GetMapping("/myPastOrder")
    public ResultEntity findMyPastOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size) {
        String id = getId();
        Page<Order> orderPageInfo = orderService.findMyPastOrders(id, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }
    /*
    * 通过gymId查询我的订单
    * */

    @GetMapping("/myOrderByGym")
    public ResultEntity findMyOrdersByGym(@RequestParam String type, @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "5") Integer size){
        String id = getId();
        Page<Order> orderPageInfo = orderService.findMyOrdersByGym(id, type, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    /*
    * 发布订单，封装为OrderRequest对象，需要四个参数
    * gymId: String
    * date: String 格式：yyyy/MM/dd
    * startTime: Integer，范围16-39
    * endTime: Integer，范围17-40
    * 确保1<=endTime-startTime<=4，后端不校验
    * 发布订单后，订单会生成默认评语和默认评分，并且将字段valid设为false，表示此单未评价
    * 调用下一个comment方法进行评价订单
    * */
    @PostMapping("/addOrder")
    public ResultEntity addOrder(@Validated @RequestBody OrderRequest orderRequest, BindingResult result){
        ParamUtil.verifyParam(result);
        String email = getId();
        orderService.addOrder(orderRequest, email);
        return ResultEntity.succeed();
    }
    /*
    * 评价订单，仅限于对未评价的订单进行评价（通过valid字段判断，如果为false，则表示未评价）
    * 评价后，valid字段会变为true
    * 评价封装成对象OrderCommentRequest
    * orderId：String
    * score：Integer，范围1-5
    * comment：String 评语
    * */

    @PostMapping("/comment")
    public ResultEntity commentOrder(@Validated @RequestBody OrderCommentRequest request, BindingResult result){
        ParamUtil.verifyParam(result);
        orderService.commentOrder(request.getOrderId(), request.getScore(), request.getComment());
        return ResultEntity.succeed();
    }

    /*
    * 取消订单
    * */
    @PostMapping("cancelMyOrder")
    public ResultEntity cancelOrder(@RequestParam String orderId){
        orderService.cancelOrder(orderId);
        return ResultEntity.succeed();
    }

    /*
    * 查看所有订单
    * */
    @GetMapping("/allOrder")
    @AdminOnly
    public ResultEntity findAllOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        Page<Order> orderPageInfo = orderService.findAllOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    /*
    * 查看所有当前进行的订单
    * */
    @GetMapping("/currentOrder")
    @AdminOnly
    public ResultEntity findAllCurrentOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        Page<Order> orderPageInfo = orderService.findAllCurrentOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    /*
    * 查看所有将来的订单
    * */
    @GetMapping("/futureOrder")
    @AdminOnly
    public ResultEntity findAllFutureOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        Page<Order> orderPageInfo = orderService.findAllFutureOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    /*
     * 查看所有过去的订单
     * */
    @GetMapping("/pastOrder")
    @AdminOnly
    public ResultEntity findAllPastOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        Page<Order> orderPageInfo = orderService.findAllPastOrders(page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    /*
    * 根据场馆id来查询订单
    * */
    @GetMapping("/gymOrder")
    @AdminOnly
    public ResultEntity findOrdersByGym(@RequestParam String gymId, @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "5") Integer size){
        Page<Order> orderPageInfo = orderService.findAllOrdersByGymId(gymId, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

    /*
    * 用于管理员根据email查询其订单
    * */
    @GetMapping("/userOrder")
    @AdminOnly
    public ResultEntity findOrdersByUser(@RequestParam String email,@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "5") Integer size){
        Page<Order> orderPageInfo = orderService.findOrderByEmail(email, page, size);
        return ResultEntity.succeed(orderPageInfo);
    }

//    @PostMapping("/testOnly")
//    @LoginRequired(value = false)
//    public ResultEntity insertOrder(@RequestBody OrderRequest request){
//        orderService.testInsert(request);
//        return ResultEntity.succeed();
//    }
}

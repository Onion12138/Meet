package com.ecnu.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.domain.User;
import com.ecnu.dto.AvailableTimeRequest;
import com.ecnu.service.OrderService;
import com.ecnu.utils.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author onion
 * @date 2019/12/26 -2:01 下午
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;

    private String token;
    private String adminToken;
    @Before
    public void init(){
        User user = new User();
        User admin = new User();
        user.setEmail("969023014@qq.com");
        user.setNickname("onion");
        user.setAdmin(false);
        token = JwtUtil.createJwt(user);
        admin.setEmail(user.getEmail());
        admin.setNickname(user.getNickname());
        admin.setAdmin(true);
        adminToken = JwtUtil.createJwt(admin);
    }
    @Test
    @DisplayName("用户查看可以预约的时间")
    public void testFindAvailableTime() throws Exception {
        AvailableTimeRequest timeRequest = new AvailableTimeRequest();
        timeRequest.setDate("2019/12/25");
        timeRequest.setGymId("1576735101615918632");
        String content = JSON.toJSONString(timeRequest);
        ResultActions perform = mockMvc.perform(post("/order/available").contentType(MediaType.APPLICATION_JSON).content(content)
                .header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findAvailableTime(any());
    }
    @Test
    @DisplayName("用户查看我的全部订单")
    public void testFindMyOrders() throws Exception {
        ResultActions perform = mockMvc.perform(get("/order/myOrder").header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findOrderByEmail(anyString(), anyInt(), anyInt());
    }
    @Test
    @DisplayName("用户查看我的当前订单")
    public void testFindMyCurrentOrders() throws Exception {
        ResultActions perform = mockMvc.perform(get("/order/myCurrentOrder").header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findMyCurrentOrders(anyString(), anyInt(), anyInt());
    }
    @Test
    @DisplayName("用户查看我未来的订单")
    public void testFindMyFutureOrders() throws Exception {
        ResultActions perform = mockMvc.perform(get("/order/myFutureOrder").header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findMyFutureOrders(anyString(), anyInt(), anyInt());
    }
    @Test
    @DisplayName("用户查看我过去的订单")
    public void testFindMyPastOrders() throws Exception {
        ResultActions perform = mockMvc.perform(get("/order/myPastOrder").header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findMyPastOrders(anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("用户通过场馆类型查看订单")
    public void testFindMyOrdersByGym() throws Exception {
        ResultActions perform = mockMvc.perform(get("/order/myOrderByGym").header("user_token",token).param("type", "乒乓球"));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findMyOrdersByGym(anyString(), anyString(), anyInt(), anyInt());
    }

//
//    /*
//     * 发布订单，封装为OrderRequest对象，需要四个参数
//     * gymId: String
//     * date: String 格式：yyyy/MM/dd
//     * startTime: Integer，范围16-39
//     * endTime: Integer，范围17-40
//     * 确保1<=endTime-startTime<=4，后端不校验
//     * 发布订单后，订单会生成默认评语和默认评分，并且将字段valid设为false，表示此单未评价
//     * 调用下一个comment方法进行评价订单
//     * */
//    @PostMapping("/addOrder")
//    @VerifyParams
//    public ResultEntity addOrder(@Validated @RequestBody OrderRequest orderRequest, BindingResult result){
//        String email = getId();
//        orderService.addOrder(orderRequest, email);
//        return ResultEntity.succeed();
//    }
//    /*
//     * 评价订单，仅限于对未评价的订单进行评价（通过valid字段判断，如果为false，则表示未评价）
//     * 评价后，valid字段会变为true
//     * 评价封装成对象OrderCommentRequest
//     * orderId：String
//     * score：Integer，范围1-5
//     * comment：String 评语
//     * */
//
//    @PostMapping("/comment")
//    @VerifyParams
//    public ResultEntity commentOrder(@Validated @RequestBody OrderCommentRequest request, BindingResult result){
//        orderService.commentOrder(request.getOrderId(), request.getScore(), request.getComment());
//        return ResultEntity.succeed();
//    }
//
//    /*
//     * 取消订单
//     * */
//    @PostMapping("cancelMyOrder")
//    public ResultEntity cancelOrder(@RequestParam String orderId){
//        orderService.cancelOrder(orderId);
//        return ResultEntity.succeed();
//    }
//
//    /*
//     * 查看所有订单
//     * */
//    @GetMapping("/allOrder")
//    @AdminOnly
//    public ResultEntity findAllOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
//        Page<Order> orderPageInfo = orderService.findAllOrders(page, size);
//        return ResultEntity.succeed(orderPageInfo);
//    }
//
//    /*
//     * 查看所有当前进行的订单
//     * */
//    @GetMapping("/currentOrder")
//    @AdminOnly
//    public ResultEntity findAllCurrentOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
//        Page<Order> orderPageInfo = orderService.findAllCurrentOrders(page, size);
//        return ResultEntity.succeed(orderPageInfo);
//    }
//
//    /*
//     * 查看所有将来的订单
//     * */
//    @GetMapping("/futureOrder")
//    @AdminOnly
//    public ResultEntity findAllFutureOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
//        Page<Order> orderPageInfo = orderService.findAllFutureOrders(page, size);
//        return ResultEntity.succeed(orderPageInfo);
//    }
//
//    /*
//     * 查看所有过去的订单
//     * */
//    @GetMapping("/pastOrder")
//    @AdminOnly
//    public ResultEntity findAllPastOrders(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
//        Page<Order> orderPageInfo = orderService.findAllPastOrders(page, size);
//        return ResultEntity.succeed(orderPageInfo);
//    }
//
//    /*
//     * 根据场馆id来查询订单
//     * */
//    @GetMapping("/gymOrder")
//    @AdminOnly
//    public ResultEntity findOrdersByGym(@RequestParam String gymId, @RequestParam(defaultValue = "1") Integer page,
//                                        @RequestParam(defaultValue = "5") Integer size){
//        Page<Order> orderPageInfo = orderService.findAllOrdersByGymId(gymId, page, size);
//        return ResultEntity.succeed(orderPageInfo);
//    }
//
//    /*
//     * 用于管理员根据email查询其订单
//     * */
//    @GetMapping("/userOrder")
//    @AdminOnly
//    public ResultEntity findOrdersByUser(@RequestParam String email,@RequestParam(defaultValue = "1") Integer page,
//                                         @RequestParam(defaultValue = "5") Integer size){
//        Page<Order> orderPageInfo = orderService.findOrderByEmail(email, page, size);
//        return ResultEntity.succeed(orderPageInfo);
//    }

}

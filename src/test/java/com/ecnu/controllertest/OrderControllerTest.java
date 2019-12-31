package com.ecnu.controllertest;

import com.alibaba.fastjson.JSON;
import com.ecnu.domain.User;
import com.ecnu.request.AvailableTimeRequest;
import com.ecnu.request.OrderCommentRequest;
import com.ecnu.request.OrderRequest;
import com.ecnu.servicetest.OrderService;
import com.ecnu.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author onion
 * @date 2019/12/26 -2:01 下午
 */
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private String token;
    private String adminToken;
    @BeforeEach
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
        redisTemplate.opsForValue().set(token, user.getEmail());
        redisTemplate.opsForValue().set(adminToken, admin.getEmail());
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

    @Test
    @DisplayName("用户提交订单")
    public void testAddOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setGymId("1576735101615918632");
        orderRequest.setUserEmail("969023014@qq.com");
        orderRequest.setDate("2019/12/25");
        orderRequest.setStartTime(18);
        orderRequest.setEndTime(20);
        String content = JSON.toJSONString(orderRequest);
        ResultActions perform = mockMvc.perform(post("/order/addOrder").header("user_token",token).contentType(MediaType.APPLICATION_JSON).content(content));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).addOrder(any(), anyString());
    }

    @Test
    @DisplayName("用户评价订单")
    public void testComment() throws Exception {
        OrderCommentRequest commentRequest = new OrderCommentRequest();
        commentRequest.setComment("you can really dance");
        commentRequest.setScore(5);
        commentRequest.setOrderId("1576857609777930559");
        String content = JSON.toJSONString(commentRequest);
        ResultActions perform = mockMvc.perform(post("/order/comment").header("user_token",token).contentType(MediaType.APPLICATION_JSON).content(content));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).commentOrder(anyString(), anyInt(), anyString());
    }

    @Test
    @DisplayName("用户取消订单")
    public void testCancelMyOrder() throws Exception {
        String orderId = "1576857609777930559";
        ResultActions perform = mockMvc.perform(post("/order/cancelMyOrder").header("user_token",token).param("orderId", orderId));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).cancelOrder(anyString());
    }

    @Test
    @DisplayName("管理员查看所有订单")
    public void testFindAllOrders() throws Exception{
        ResultActions perform = mockMvc.perform(get("/order/allOrder").header("user_token",adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findAllOrders(anyInt(), anyInt());
    }

    @Test
    @DisplayName("管理员查看正在进行的订单")
    public void testFindCurrentOrders() throws Exception{
        ResultActions perform = mockMvc.perform(get("/order/currentOrder").header("user_token",adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findAllCurrentOrders(anyInt(), anyInt());
    }
    @Test
    @DisplayName("管理员查看将来的订单")
    public void testFindFutureOrders() throws Exception{
        ResultActions perform = mockMvc.perform(get("/order/futureOrder").header("user_token",adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findAllFutureOrders(anyInt(), anyInt());
    }

    @Test
    @DisplayName("管理员查看过去的订单")
    public void testFindPastOrders() throws Exception{
        ResultActions perform = mockMvc.perform(get("/order/pastOrder").header("user_token",adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findAllPastOrders(anyInt(), anyInt());
    }

    @Test
    @DisplayName("管理员通过场馆类型查找订单")
    public void testFindOrdersByType() throws Exception{
        ResultActions perform = mockMvc.perform(get("/order/gymOrder").header("user_token",adminToken).param("type","乒乓球"));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findAllOrdersByType(anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("管理员通过用户查找订单")
    public void testFindOrdersByUser() throws Exception{
        ResultActions perform = mockMvc.perform(get("/order/userOrder").header("user_token",adminToken).param("email","969023014@qq.com"));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findOrderByEmail(anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("用户查找自己取消的订单")
    public void testFindMyCanceledOrders() throws Exception {
        ResultActions perform = mockMvc.perform(get("/order/myCanceledOrder").header("user_token", token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findMyCanceledOrder(anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("管理员查找所有取消的订单")
    public void testFindAllCanceledOrders() throws Exception {
        ResultActions perform = mockMvc.perform(get("/order/allCanceledOrder").header("user_token", adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(orderService).findAllCanceledOrders(anyInt(), anyInt());
    }
}

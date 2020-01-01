package com.ecnu.service;

import com.ecnu.dao.OrderDao;
import com.ecnu.dao.OrderMapper;
import com.ecnu.domain.Order;
import com.ecnu.request.AvailableTimeRequest;
import com.ecnu.request.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.ecnu.service.impl.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author onion
 * @date 2019/12/31 -10:27 上午
 */
@SpringBootTest
public class OrderServiceTest {
    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderMapper orderMapper;
    private String email = "969023014";
    private String gymId = "1576735101615918632";
    private String orderId = "1576737319789510898";
    @Test
    @DisplayName("测试通过Email查询订单")
    public void testFindOrderByEmail() {
        orderService.findOrderByEmail(email, 1, 5);
        PageRequest request = PageRequest.of(0, 5);
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<PageRequest> requestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        verify(orderDao).findAllByUserEmail(emailCaptor.capture(), requestCaptor.capture());
        assertAll(() -> assertEquals(email, emailCaptor.getValue()),
                () -> assertEquals(request.getPageNumber(), requestCaptor.getValue().getPageNumber()),
                () -> assertEquals(request.getPageSize(), requestCaptor.getValue().getPageSize())
        );
    }
    @Test
    @DisplayName("测试通过Email查询正在进行中的订单")
    public void testFindMyCurrentOrders() {
        orderService.findMyCurrentOrders(email, 1, 5);
        verify(orderDao).findAllByUserEmailAndStartTimeBeforeAndEndTimeAfter(anyString(), any(LocalDateTime.class), any(LocalDateTime.class),any(PageRequest.class));
    }
    @Test
    @DisplayName("测试通过Email查询将来中的订单")
    public void testFindMyFutureOrders() {
        orderService.findMyFutureOrders(email, 1, 5);
        verify(orderDao).findAllByUserEmailAndStartTimeAfter(anyString(), any(LocalDateTime.class), any(PageRequest.class));
    }
    @Test
    @DisplayName("测试通过Email查询过去的订单")
    public void testFindMyPastOrders() {
        orderService.findMyPastOrders(email, 1, 5);
        verify(orderDao).findAllByUserEmailAndEndTimeBefore(anyString(), any(LocalDateTime.class), any(PageRequest.class));
    }
    @Test
    @DisplayName("测试通过场馆类型查找订单")
    public void testFindMyOrdersByGym() {
        orderService.findMyOrdersByGym(email, "乒乓球", 1, 5);
        verify(orderDao).findAllByUserEmailAndType(anyString(), anyString(), any(PageRequest.class));

    }
    @Test
    @DisplayName("测试添加订单")
    public void testAddOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setGymId(gymId);
        orderRequest.setUserEmail(email);
        orderRequest.setDate("2019/12/31");
        orderRequest.setStartTime(16);
        orderRequest.setEndTime(19);
        orderService.addOrder(orderRequest, email);
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderDao).save(captor.capture());
        assertAll(() -> assertNotNull(captor.getValue().getOrderDate()),
                () -> assertEquals(5, captor.getValue().getScore()),
                () -> assertEquals("默认好评", captor.getValue().getComment()),
                () -> assertEquals(8, captor.getValue().getStartTime().getHour()),
                () -> assertEquals(0, captor.getValue().getStartTime().getMinute()),
                () -> assertEquals(9, captor.getValue().getEndTime().getHour()),
                () -> assertEquals(30, captor.getValue().getEndTime().getMinute()),
                () -> assertFalse(captor.getValue().getCancel()),
                () -> assertFalse(captor.getValue().getValid()),
                () -> assertEquals(email, captor.getValue().getUserEmail()),
                () -> assertEquals(gymId, captor.getValue().getGymId()),
                () -> assertFalse(StringUtils.isEmpty(captor.getValue().getOrderId()))
        );
    }
    @Test
    @DisplayName("测试取消订单")
    public void testCancelOrder() {
        orderService.cancelOrder(orderId);
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderMapper).updateByPrimaryKeySelective(captor.capture());
        assertEquals(orderId, captor.getValue().getOrderId());
        assertTrue(captor.getValue().getCancel());
    }
    @Test
    @DisplayName("测试查找全部订单")
    public void testFindAllOrders() {
        orderService.findAllOrders(1, 5);
        verify(orderDao).findAll(any(PageRequest.class));
    }
    @Test
    @DisplayName("测试查找全部正在进行的订单")
    public void testFindAllCurrentOrders() {
        orderService.findAllCurrentOrders(1, 5);
        verify(orderDao).findAllByStartTimeBeforeAndEndTimeAfter(any(LocalDateTime.class), any(LocalDateTime.class), any(PageRequest.class));
    }
    @Test
    @DisplayName("测试查找全部将来的订单")
    public void testFindAllFutureOrders() {
        orderService.findAllFutureOrders(1, 5);
        verify(orderDao).findAllByStartTimeAfter(any(LocalDateTime.class), any(PageRequest.class));
    }

    @Test
    @DisplayName("测试查找全部过去的订单")
    public void testFindAllPastOrders() {
        orderService.findAllPastOrders(1, 5);
        verify(orderDao).findAllByEndTimeBefore(any(LocalDateTime.class), any(PageRequest.class));
    }
    @Test
    @DisplayName("测试通过场馆类型查找全部订单")
    public void testFindAllOrdersByType() {
        orderService.findAllOrdersByType("乒乓球",1,5);
        verify(orderDao).findAllByType(anyString(), any(PageRequest.class));
    }
    @Test
    @DisplayName("测试评价订单")
    public void testCommentOrder() {
        int score = 4;
        String comment = "just so so";
        orderService.commentOrder(orderId, score, comment);
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderMapper).updateByPrimaryKeySelective(captor.capture());
        assertAll(()-> assertEquals(score, captor.getValue().getScore()),
                () -> assertEquals(comment, captor.getValue().getComment()),
                () -> assertEquals(orderId, captor.getValue().getOrderId()),
                () -> assertTrue(captor.getValue().getValid()));
    }
    @Test
    @DisplayName("测试查找可预约的时间")
    public void testFindAvailableTime() {
        AvailableTimeRequest request = new AvailableTimeRequest();
        request.setGymId(gymId);
        request.setDate("2019/12/20");
        Order order = new Order();
        LocalDate date = LocalDate.of(2019, 12, 20);
        order.setOrderDate(date);
        order.setStartTime(LocalDateTime.of(date, LocalTime.of(8, 30)));
        order.setEndTime(LocalDateTime.of(date, LocalTime.of(9, 0)));
        Order order2 = new Order();
        order2.setOrderDate(date);
        order2.setStartTime(LocalDateTime.of(date, LocalTime.of(17, 0)));
        order2.setEndTime(LocalDateTime.of(date, LocalTime.of(17, 30)));
        when(orderMapper.selectByExample(any(Example.class))).thenReturn(Arrays.asList(order, order2));
        List<Integer[]> availableTime = orderService.findAvailableTime(request);
        verify(orderMapper).selectByExample(any(Example.class));
        assertAll(() -> assertEquals(17, availableTime.get(0)[0]),
                () -> assertEquals(18, availableTime.get(0)[1]),
                () -> assertEquals(34, availableTime.get(1)[0]),
                () -> assertEquals(35, availableTime.get(1)[1])
        );
    }
    /*
     List<Order> orderList = orderMapper.selectByExample(example);
        List<Integer[]> interval = new ArrayList<>();
        orderList.stream().map(this::timeToInterval).forEach(interval::add);
        return interval;
    * **/
    @Test
    @DisplayName("测试查找取消的订单")
    public void testFindMyCanceledOrder() {
        orderService.findMyCanceledOrder(email,1, 5);
        verify(orderDao).findByUserEmailAndCancelTrue(anyString(), any(Pageable.class));
    }
    @Test
    @DisplayName("测试查找全部取消的订单")
    public void testFindAllCanceledOrders() {
        orderService.findAllCanceledOrders(1, 5);
        verify(orderDao).findAllByCancelTrue(any(Pageable.class));
    }

}

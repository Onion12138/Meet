package com.ecnu.servicetest;

import com.ecnu.daotest.GymDao;
import com.ecnu.daotest.GymMapper;
import com.ecnu.domain.Gym;
import com.ecnu.domain.Order;
import com.ecnu.request.GymFilterRequest;
import com.ecnu.servicetest.impl.GymServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StringUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
/**
 * @author onion
 * @date 2019/12/30 -10:27 下午
 */
@SpringBootTest
public class GymServiceTest {
    @Mock
    private GymMapper gymMapper;
    @Mock
    private GymDao gymDao;
    @InjectMocks
    private GymServiceImpl gymService;
    @Test
    @DisplayName("测试查找所有场馆")
    public void testFindAllGyms() {
        gymService.findAllGyms(1, 5);
        verify(gymMapper).selectAll();
    }
    @Test
    @DisplayName("测试通过关键词查询场馆")
    public void testFindGymsByKeyword() {
        gymService.findGymsByKeyword(1, 5, "keyword");
        verify(gymMapper).selectByExample(any());
    }
    @Test
    @DisplayName("测试通过过滤器查询场馆")
    public void testFindGymsByFilter() {
        GymFilterRequest request = new GymFilterRequest();
        request.setOpenOnly(true);
        request.setHighToLow(true);
        request.setType("乒乓球");
        request.setAddress("大学生活动中心");
        gymService.findGymsByFilter(1,5, request);
        verify(gymMapper).selectByExample(any());
    }
    @Test
    @DisplayName("测试查看某个场馆的评分,无订单时")
    public void testFindScoreOfEmptyOrder() {
        Gym gym = new Gym();
        when(gymDao.findById(any())).thenReturn(Optional.of(gym));
        Map<String, Object> map = gymService.findScore("1576735101615918632");
        assertEquals(0, map.get("score"));
        assertEquals("暂无订单和评论", map.get("comment"));
    }
    @Test
    @DisplayName("测试查看某个场馆的评分,有订单时")
    public void testFindScore() {
        Gym gym = new Gym();
        Set<Order> orderSet = new HashSet<>();
        Order order = new Order();
        order.setScore(4);
        order.setComment("下次还会再来");
        orderSet.add(order);
        Order order2 = new Order();
        order2.setScore(5);
        order2.setComment("服务至上");
        orderSet.add(order2);
        gym.setOrderSet(orderSet);
        when(gymDao.findById(any())).thenReturn(Optional.of(gym));
        Map<String, Object> map = gymService.findScore("1576735101615918632");
        assertEquals(4.5, map.get("score"));
        assertEquals(2, ((List) map.get("comment")).size());
    }

    @Test
    @DisplayName("测试添加场馆")
    public void testAddGym() {
        Gym gym = new Gym();
        gymService.addGym(gym);
        ArgumentCaptor<Gym> captor = ArgumentCaptor.forClass(Gym.class);
        verify(gymMapper).insert(captor.capture());
        assertTrue(captor.getValue().getOpen());
        assertFalse(StringUtils.isEmpty(captor.getValue().getGymId()));
    }

    @Test
    @DisplayName("测试更新场馆")
    public void testUpdateGym() {
        Gym gym = new Gym();
        gymService.updateGym(gym);
        verify(gymMapper).updateByPrimaryKeySelective(gym);
    }
    @Test
    @DisplayName("测试删除场馆")
    public void testDeleteGym() {
        gymService.deleteGym("1576735101615918632");
        ArgumentCaptor<Gym> captor = ArgumentCaptor.forClass(Gym.class);
        verify(gymMapper).updateByPrimaryKeySelective(captor.capture());
        assertFalse(captor.getValue().getOpen());
    }
}

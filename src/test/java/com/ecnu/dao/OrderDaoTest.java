package com.ecnu.dao;

import com.ecnu.domain.Gym;
import com.ecnu.domain.Order;
import com.ecnu.domain.User;
import com.ecnu.utils.KeyUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author onion
 * @date 2019/12/31 -11:32 上午
 */
@DataJpaTest
@DirtiesContext
@Transactional
@RunWith(SpringRunner.class)
public class OrderDaoTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private OrderDao orderDao;
    private String email = "969023014@qq.com";
    private Pageable pageable;
    @Before
    public void init() {
        User user = new User();
        user.setEmail(email);
        User user2 = new User();
        String email2 = "969023015@qq.com";
        user2.setEmail(email2);
        Gym gym1 = new Gym();
        gym1.setGymId("1576735101615918632");
        gym1.setType("乒乓球");
        Gym gym2 = new Gym();
        gym2.setGymId("1576735204701168411");
        gym2.setType("篮球");
        Order order1 = new Order();
        Order order2 = new Order();
        Order order3 = new Order();
        Order order4 = new Order();
        LocalDate now = LocalDate.now();
        order1.setOrderDate(now);
        order1.setStartTime(LocalDateTime.of(now, LocalTime.of(7, 30)));
        order1.setEndTime(LocalDateTime.of(now, LocalTime.of(8, 0)));
        order1.setCancel(false);
        String gymId = "1576735101615918632";
        order1.setGymId(gymId);
        order1.setUserEmail(email);
        order1.setOrderId(KeyUtil.genUniqueKey());

        order2.setOrderDate(now);
        order2.setStartTime(LocalDateTime.of(now, LocalTime.of(20, 30)));
        order2.setEndTime(LocalDateTime.of(now, LocalTime.of(21, 0)));
        order2.setCancel(false);
        String gymId2 = "1576735204701168411";
        order2.setGymId(gymId2);
        order2.setUserEmail(email);
        order2.setOrderId(KeyUtil.genUniqueKey());

        order3.setOrderDate(now);
        order3.setStartTime(LocalDateTime.of(now, LocalTime.of(11, 30)));
        order3.setEndTime(LocalDateTime.of(now, LocalTime.of(12, 30)));
        order3.setCancel(false);
        order3.setGymId(gymId);
        order3.setUserEmail(email);
        order3.setOrderId(KeyUtil.genUniqueKey());

        order4.setOrderDate(now);
        order4.setStartTime(LocalDateTime.of(now, LocalTime.of(16, 30)));
        order4.setEndTime(LocalDateTime.of(now, LocalTime.of(17, 0)));
        order4.setCancel(true);
        order4.setGymId(gymId2);
        order4.setUserEmail(email2);
        order4.setOrderId(KeyUtil.genUniqueKey());

        entityManager.persist(user);
        entityManager.persist(user2);
        entityManager.persist(gym1);
        entityManager.persist(gym2);
        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.persist(order3);
        entityManager.persist(order4);
    }
    @Test
    @DisplayName("测试通过用户邮箱查询订单")
    public void testFindAllByUserEmail() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAllByUserEmail(email, pageable);
        assertEquals(3, page.getTotalElements());
    }
    @Test
    @DisplayName("测试通过用户邮箱查询现在的订单")
    public void testFindAllCurrentOrderByUserEmail() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAllByUserEmailAndStartTimeBeforeAndEndTimeAfter(email, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)),pageable);
        assertEquals(1, page.getTotalElements());
    }
    @Test
    @DisplayName("测试通过用户邮箱查询将来的订单")
    public void testFindAllFutureOrderByUserEmail() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAllByUserEmailAndStartTimeAfter(email, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)),pageable);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    @DisplayName("测试通过用户邮箱查询过去的订单")
    public void testFindAllPastOrderByUserEmail() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAllByUserEmailAndEndTimeBefore(email, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)),pageable);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    @DisplayName("测试通过用户邮箱和场馆类型查询订单")
    public void testFindAllByUserEmailAndType() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAllByUserEmailAndType(email, "乒乓球", pageable);
        assertEquals(2, page.getTotalElements());
    }

    @Test
    @DisplayName("测试通过场馆类型查询订单")
    public void testFindAllByType() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAllByType("篮球", pageable);
        assertEquals(2, page.getTotalElements());
    }
    @Test
    @DisplayName("测试查询所有订单")
    public void testFindAll() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAll( pageable);
        assertEquals(4, page.getTotalElements());
    }
    @Test
    @DisplayName("测试查询现在的订单")
    public void testFindAllCurrent() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAllByStartTimeBeforeAndEndTimeAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)),
                LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)),pageable);
        assertEquals(1, page.getTotalElements());
    }
    @Test
    @DisplayName("测试查询将来的订单")
    public void testFindAllFuture() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAllByStartTimeAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)), pageable);
        assertEquals(2, page.getTotalElements());
    }
    @Test
    @DisplayName("测试查询过去的订单")
    public void testFindAllPast() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAllByEndTimeBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)), pageable);
        assertEquals(1, page.getTotalElements());
    }
    @Test
    @DisplayName("测试查询用户取消的订单")
    public void testFindUserCanceledOrder() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findByUserEmailAndCancel(email, true, pageable);
        assertEquals(0, page.getTotalElements());
    }
    @Test
    @DisplayName("测试查询用户取消的订单")
    public void testFindAllCanceledOrders() {
        pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findByCancel(true, pageable);
        assertEquals(1, page.getTotalElements());
    }
}

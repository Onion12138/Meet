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
    private Order order1;
    private Order order2;
    private Order order3;
    private Order order4;
    private User user;
    private Gym gym1;
    private Gym gym2;
    private String email = "969023014@qq.com";
    private String gymId = "1576735101615918632";
    private String gymId2 = "1576735204701168411";
    @Before
    public void init() {
        user = new User();
        user.setEmail(email);
        gym1 = new Gym();
        gym1.setGymId("1576735101615918632");
        gym1.setType("乒乓球");
        gym2 = new Gym();
        gym2.setGymId("1576735204701168411");
        gym2.setType("篮球");
        order1 = new Order();
        order2 = new Order();
        order3 = new Order();
        order4 = new Order();
        LocalDate now = LocalDate.now();
        order1.setOrderDate(now);
        order1.setStartTime(LocalDateTime.of(now, LocalTime.of(7, 30)));
        order1.setEndTime(LocalDateTime.of(now, LocalTime.of(8, 0)));
        order1.setCancel(false);
        order1.setGymId(gymId);
        order1.setUserEmail(email);
        order1.setOrderId(KeyUtil.genUniqueKey());

        order2.setOrderDate(now);
        order2.setStartTime(LocalDateTime.of(now, LocalTime.of(20, 30)));
        order2.setEndTime(LocalDateTime.of(now, LocalTime.of(21, 0)));
        order2.setCancel(false);
        order2.setGymId(gymId2);
        order2.setUserEmail(email);
        order2.setOrderId(KeyUtil.genUniqueKey());

        order3.setOrderDate(now);
        order3.setStartTime(LocalDateTime.of(now, LocalTime.of(7, 30)));
        order3.setEndTime(LocalDateTime.of(now, LocalTime.of(8, 0)));
        order3.setCancel(false);
        order3.setGymId(gymId);
        order3.setUserEmail(email);
        order3.setOrderId(KeyUtil.genUniqueKey());

        order4.setOrderDate(now);
        order4.setStartTime(LocalDateTime.of(now, LocalTime.of(7, 30)));
        order4.setEndTime(LocalDateTime.of(now, LocalTime.of(8, 0)));
        order4.setCancel(true);
        order4.setGymId(gymId2);
        order4.setUserEmail(email);
        order4.setOrderId(KeyUtil.genUniqueKey());

        entityManager.persist(user);
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
        Pageable pageable = PageRequest.of(0, 2);
        Page<Order> page = orderDao.findAllByUserEmail(email, pageable);
        assertEquals(2, page.getTotalPages());
    }
//    Page<Order> findAllByUserEmail(String email, Pageable pageable);
//    Page<Order> findAllByUserEmailAndStartTimeBeforeAndEndTimeAfter(String email, LocalDateTime start, LocalDateTime end, Pageable pageable);
//    Page<Order> findAllByUserEmailAndStartTimeAfter(String email, LocalDateTime start, Pageable pageable);
//    Page<Order> findAllByUserEmailAndEndTimeBefore(String email, LocalDateTime end, Pageable pageable);
//    @Query(nativeQuery = true, value = "select * from orders, gym where user_email = ?1 and type = ?2 and order_gym_id = gym_id")
//    Page<Order> findAllByUserEmailAndType(String email, String type, Pageable pageable);
//    @Query(nativeQuery = true, value = "select * from orders, gym where type = ?1 and order_gym_id = gym_id")
//    Page<Order> findAllByType(String type, Pageable pageable);
//    Page<Order> findAll(Pageable pageable);
//    Page<Order> findAllByStartTimeBeforeAndEndTimeAfter(LocalDateTime start, LocalDateTime end, Pageable pageable);
//    Page<Order> findAllByStartTimeAfter(LocalDateTime start, Pageable pageable);
//    Page<Order> findAllByEndTimeBefore(LocalDateTime end, Pageable pageable);
//    Page<Order> findByUserEmailAndCancel(String email, boolean cancel, Pageable pageable);
//    Page<Order> findByCancel(boolean b, Pageable pageable);
}

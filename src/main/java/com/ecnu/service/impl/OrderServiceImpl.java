package com.ecnu.service.impl;

import com.ecnu.dao.OrderDao;
import com.ecnu.dao.OrderMapper;
import com.ecnu.domain.Order;
import com.ecnu.dto.OrderRequest;
import com.ecnu.service.OrderService;
import com.ecnu.utils.KeyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/11 -11:49 下午
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDao orderDao;
    @Override
    public PageInfo<Order> findOrdersByUserId(String email, int page, int size) {
        PageHelper.startPage(page, size);
        Order order = new Order();
        order.setUserEmail(email);
        List<Order> orders = orderMapper.select(order);
        return new PageInfo<>(orders);
    }

    @Override
    public PageInfo<Order> findMyCurrentOrders(String email, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userEmail", email);
        criteria.andLessThan("startTime", LocalDateTime.now());
        criteria.andGreaterThan("endTime", LocalDateTime.now());
        return new PageInfo<>(orderMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Order> findMyFutureOrders(String email, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userEmail", email);
        criteria.andGreaterThan("startTime", LocalDateTime.now());
        return new PageInfo<>(orderMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Order> findMyPastOrders(String email, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userEmail", email);
        criteria.andLessThan("endTime", LocalDateTime.now());
        return new PageInfo<>(orderMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Order> findMyOrdersByGym(String id, String gymId, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", id).andEqualTo("gymId", gymId);
        example.and(criteria);
        List<Order> orders = orderMapper.selectByExample(example);
        return new PageInfo<>(orders);
    }

    @Override
    public PageInfo<Order> findMyOrdersGroupByGym(String id, Integer page, Integer size) {
        PageHelper.startPage(page,size);
//        Example example = new Example(Order.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.
//        orderMapper.selectByExample();
        return null;
    }

    @Override
    public void addOrder(OrderRequest request, String email) {
        Order order = new Order();
        order.setOrderId(KeyUtil.genUniqueKey());
        order.setGymId(request.getGymId());
        order.setUserEmail(email);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(request.getDate(), formatter);
        order.setOrderDate(localDate);
        order.setScore(5);
        order.setComment("默认好评");
        order.setStartTime(LocalDateTime.of(localDate, LocalTime.of(request.getStartTime() / 2, request.getStartTime() % 2 == 1 ? 30 : 0)));
        order.setEndTime(LocalDateTime.of(localDate, LocalTime.of(request.getEndTime() / 2, request.getEndTime() % 2 == 1 ? 30 : 0)));
        order.setCancel(false);
        order.setValid(true);
        orderDao.save(order);
    }

    //是否不需要userId
    @Override
    public void cancelOrder(String userId, String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setCancel(true);
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public PageInfo<Order> findAllOrders(Integer page, Integer size) {
        return null;
    }

    @Override
    public PageInfo<Order> findAllCurrentOrders(Integer page, Integer size) {
        return null;
    }

    @Override
    public PageInfo<Order> findAllFutureOrders(Integer page, Integer size) {
        return null;
    }

    @Override
    public PageInfo<Order> findAllPastOrders(Integer page, Integer size) {
        return null;
    }

    @Override
    public PageInfo<Order> findAllOrdersByGymId(String gymId, Integer page, Integer size) {
        return null;
    }

    @Override
    public void cancelOrders(Set<String> orderIds) {

    }

    @Override
    public void commentOrder(String userId, Integer score, String comment) {
        Order order = new Order();
        order.setScore(score);
        order.setComment(comment);
        //参数不对
    }
}

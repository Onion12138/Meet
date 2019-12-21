package com.ecnu.service.impl;

import com.ecnu.dao.OrderDao;
import com.ecnu.dao.OrderMapper;
import com.ecnu.domain.Order;
import com.ecnu.dto.AvailableTimeRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        order.setValid(false);
        orderDao.save(order);
    }

    //是否不需要userId
    @Override
    public void cancelOrder(String orderId) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setCancel(true);
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public PageInfo<Order> findAllOrders(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        return new PageInfo<>(orderMapper.selectAll());
    }

    @Override
    public PageInfo<Order> findAllCurrentOrders(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLessThan("startTime", LocalDateTime.now());
        criteria.andGreaterThan("endTime", LocalDateTime.now());
        return new PageInfo<>(orderMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Order> findAllFutureOrders(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andGreaterThan("startTime", LocalDateTime.now());
        return new PageInfo<>(orderMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Order> findAllPastOrders(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLessThan("endTime", LocalDateTime.now());
        return new PageInfo<>(orderMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Order> findAllOrdersByGymId(String gymId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Order order = new Order();
        order.setGymId(gymId);
        return new PageInfo<>(orderMapper.select(order));
    }

    @Override
    public void commentOrder(String orderId, Integer score, String comment) {
        Order order = new Order();
        order.setScore(score);
        order.setComment(comment);
        order.setOrderId(orderId);
        order.setValid(true);
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public List<Integer[]> findAvailableTime(AvailableTimeRequest request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(request.getDate(), formatter);
        String gymId = request.getGymId();
        Example example = new Example(Order.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("gymId", gymId);
        criteria.andEqualTo("orderDate",localDate);
        List<Order> orderList = orderMapper.selectByExample(example);
        List<Integer[]> interval = new ArrayList<>();
        orderList.stream().map(this::timeToInterval).forEach(interval::add);
        return interval;
    }

    @Override
    public void testInsert(OrderRequest request) {
        Random random = new Random();
        Order order = new Order();
        order.setOrderId(KeyUtil.genUniqueKey());
        order.setValid(true);
        order.setGymId(request.getGymId());
        order.setCancel(false);
        order.setScore(random.nextInt(3) + 3);
        order.setComment(comment());
        order.setUserEmail(request.getUserEmail());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.parse(request.getDate(), formatter);
        order.setOrderDate(localDate);
        order.setStartTime(LocalDateTime.of(localDate, LocalTime.of(request.getStartTime() / 2, request.getStartTime() % 2 == 1 ? 30 : 0)));
        order.setEndTime(LocalDateTime.of(localDate, LocalTime.of(request.getEndTime() / 2, request.getEndTime() % 2 == 1 ? 30 : 0)));
        orderMapper.insertSelective(order);
    }
    private String comment() {
        Random random = new Random();
        String[] comments = {"环境不错","服务很好","帅哥多","美女多","价格实惠","器材陈旧","场地狭小","环境一般","气氛好","服务一般","人多","下次还来"};
        return comments[random.nextInt(comments.length)];
    }
    private Integer[] timeToInterval(Order order) {
        int start = order.getStartTime().getHour() * 2;
        start += order.getStartTime().getMinute() == 30 ? 1 : 0;
        int end = order.getEndTime().getHour() * 2;
        end += order.getEndTime().getMinute() == 30 ? 1 : 0;
        return new Integer[]{start, end};
    }
}

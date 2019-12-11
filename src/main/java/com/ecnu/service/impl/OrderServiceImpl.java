package com.ecnu.service.impl;

import com.ecnu.domain.Order;
import com.ecnu.dto.OrderRequest;
import com.ecnu.service.OrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author onion
 * @date 2019/12/11 -11:49 下午
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public PageInfo<Order> findOrdersByUserId(String id, int page, int size) {
        return null;
    }

    @Override
    public PageInfo<Order> findMyCurrentOrders(String id, Integer page, Integer size) {
        return null;
    }

    @Override
    public PageInfo<Order> findMyFutureOrders(String id, Integer page, Integer size) {
        return null;
    }

    @Override
    public PageInfo<Order> findMyPastOrders(String id, Integer page, Integer size) {
        return null;
    }

    @Override
    public PageInfo<Order> findMyOrdersByGym(String id, String gymId, Integer page, Integer size) {
        return null;
    }

    @Override
    public PageInfo<Order> findMyOrdersGroupByGym(String id, Integer page, Integer size) {
        return null;
    }

    @Override
    public void addOrder(OrderRequest request) {

    }

    @Override
    public void cancelOrder(String userId, String orderId) {

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
}

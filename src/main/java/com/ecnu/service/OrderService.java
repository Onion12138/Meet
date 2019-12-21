package com.ecnu.service;

import com.ecnu.domain.Order;
import com.ecnu.dto.AvailableTimeRequest;
import com.ecnu.dto.OrderRequest;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author onion
 * @date 2019/12/11 -6:24 下午
 */
public interface OrderService {
    PageInfo<Order> findOrdersByUserId(String id, int page, int size);

    PageInfo<Order> findMyCurrentOrders(String id, Integer page, Integer size);

    PageInfo<Order> findMyFutureOrders(String id, Integer page, Integer size);

    PageInfo<Order> findMyPastOrders(String id, Integer page, Integer size);

    PageInfo<Order> findMyOrdersByGym(String id, String gymId, Integer page, Integer size);

    void addOrder(OrderRequest request, String email);

    void cancelOrder(String orderId);

    PageInfo<Order> findAllOrders(Integer page, Integer size);

    PageInfo<Order> findAllCurrentOrders(Integer page, Integer size);

    PageInfo<Order> findAllFutureOrders(Integer page, Integer size);

    PageInfo<Order> findAllPastOrders(Integer page, Integer size);

    PageInfo<Order> findAllOrdersByGymId(String gymId, Integer page, Integer size);

    void commentOrder(String orderId, Integer score, String comment);

    List<Integer[]> findAvailableTime(AvailableTimeRequest request);

    void testInsert(OrderRequest request);
}

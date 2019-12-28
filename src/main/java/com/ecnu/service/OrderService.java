package com.ecnu.service;

import com.ecnu.domain.Order;
import com.ecnu.dto.AvailableTimeRequest;
import com.ecnu.dto.OrderRequest;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author onion
 * @date 2019/12/11 -6:24 下午
 */
public interface OrderService {
    Page<Order> findOrderByEmail(String email, int page, int size);

    Page<Order> findMyCurrentOrders(String id, Integer page, Integer size);

    Page<Order> findMyFutureOrders(String id, Integer page, Integer size);

    Page<Order> findMyPastOrders(String id, Integer page, Integer size);

    Page<Order> findMyOrdersByGym(String id, String type, Integer page, Integer size);

    void addOrder(OrderRequest request, String email);

    void cancelOrder(String orderId);

    Page<Order> findAllOrders(Integer page, Integer size);

    Page<Order> findAllCurrentOrders(Integer page, Integer size);

    Page<Order> findAllFutureOrders(Integer page, Integer size);

    Page<Order> findAllPastOrders(Integer page, Integer size);

    Page<Order> findAllOrdersByType(String type, Integer page, Integer size);

    void commentOrder(String orderId, Integer score, String comment);

    List<Integer[]> findAvailableTime(AvailableTimeRequest request);

    Page<Order> findMyCanceledOrder(String email, Integer page, Integer size);

    Page<Order> findAllCanceledOrders(Integer page, Integer size);
}

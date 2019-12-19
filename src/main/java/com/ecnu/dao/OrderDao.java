package com.ecnu.dao;

import com.ecnu.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author onion
 * @date 2019/12/19 -2:32 下午
 */
@Repository
public interface OrderDao extends JpaRepository<Order, String> {
}

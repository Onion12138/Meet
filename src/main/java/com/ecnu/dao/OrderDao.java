package com.ecnu.dao;

import com.ecnu.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * @author onion
 * @date 2019/12/19 -2:32 下午
 */
@Repository
public interface OrderDao extends JpaRepository<Order, String> {
    Page<Order> findAllByUserEmail(String email, Pageable pageable);
    Page<Order> findAllByUserEmailAndStartTimeBeforeAndEndTimeAfter(String email, LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Order> findAllByUserEmailAndStartTimeAfter(String email, LocalDateTime start, Pageable pageable);
    Page<Order> findAllByUserEmailAndEndTimeBefore(String email, LocalDateTime end, Pageable pageable);
    Page<Order> findAllByUserEmailAndGymId(String email, String gymId, Pageable pageable);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findAllByStartTimeBeforeAndEndTimeAfter(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Order> findAllByStartTimeAfter(LocalDateTime start, Pageable pageable);
    Page<Order> findAllByEndTimeBefore(LocalDateTime end, Pageable pageable);
    Page<Order> findAllByGymId(String gymId, Pageable pageable);
}

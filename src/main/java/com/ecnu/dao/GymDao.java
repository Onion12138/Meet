package com.ecnu.dao;

import com.ecnu.domain.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @date 2019/12/20 -10:28 下午
 */
@Repository
public interface GymDao extends JpaRepository<Gym, String> {
}

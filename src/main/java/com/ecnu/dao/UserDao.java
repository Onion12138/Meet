package com.ecnu.dao;

import com.ecnu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @date 2019/12/19 -1:01 下午
 */
@Repository
public interface UserDao extends JpaRepository<User, String> {
}

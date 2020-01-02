package com.ecnu.dao;

import com.ecnu.domain.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @date 2019/12/20 -11:11 下午
 */
@Repository
public interface NewsDao extends JpaRepository<News, String> {
}

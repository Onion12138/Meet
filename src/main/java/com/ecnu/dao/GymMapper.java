package com.ecnu.dao;

import com.ecnu.domain.Gym;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author onion
 * @date 2019/12/12 -1:48 下午
 */
@Repository
public interface GymMapper extends Mapper<Gym> {
}

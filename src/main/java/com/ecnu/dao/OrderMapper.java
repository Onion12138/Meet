package com.ecnu.dao;

import com.ecnu.domain.Order;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @date 2019/12/12 -7:32 下午
 */
@Repository
@org.apache.ibatis.annotations.Mapper
public interface OrderMapper extends Mapper<Order> {
}

package com.ecnu.dao;

import com.ecnu.domain.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @date 2019/12/12 -9:11 上午
 */
@Repository
@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<User> {
}

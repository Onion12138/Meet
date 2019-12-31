package com.ecnu.daotest;

import com.ecnu.domain.News;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author onion
 * @date 2019/12/12 -6:37 下午
 */
@Repository
@org.apache.ibatis.annotations.Mapper
public interface NewsMapper extends Mapper<News> {
}

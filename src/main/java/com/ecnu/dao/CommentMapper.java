package com.ecnu.dao;

import com.ecnu.domain.NewsComment;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @date 2019/12/12 -6:49 下午
 */
@Repository
@org.apache.ibatis.annotations.Mapper
public interface CommentMapper extends Mapper<NewsComment> {
}

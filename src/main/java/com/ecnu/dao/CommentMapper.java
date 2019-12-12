package com.ecnu.dao;

import com.ecnu.domain.Comment;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author onion
 * @date 2019/12/12 -6:49 下午
 */
@Repository
public interface CommentMapper extends Mapper<Comment> {
}

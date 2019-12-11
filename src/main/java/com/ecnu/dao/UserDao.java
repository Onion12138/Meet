package com.ecnu.dao;

import com.ecnu.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author onion
 * @date 2019/12/11 -6:28 下午
 */
@Mapper
@Repository
public interface UserDao {
    @Select("select * from user where email = #{email}")
    User findByEmail(String email);
    @Insert("")
    void save(User user);
    void modifyNickname(String nickname);
}

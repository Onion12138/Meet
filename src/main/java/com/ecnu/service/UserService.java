package com.ecnu.service;

import com.ecnu.domain.Order;
import com.ecnu.domain.User;
import com.ecnu.dto.UserLoginRequest;
import com.ecnu.dto.UserRegisterRequest;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/11 -6:22 下午
 */
public interface UserService {
    boolean checkEmail(String email);

    void register(UserRegisterRequest request);

    Map<String, String> login(UserLoginRequest request);

    void modifyNickname(String id, String nickname);

    String uploadProfile(String id, MultipartFile file);

    void modifyPassword(String id, String password, String code);

    void sendCode(String email);

    PageInfo<User> findAllUsers(int page, int size);

    void disableAccount(String userId);

    void updateCredit(String userId, Integer credit);

    void enableAccount(String userId);

    PageInfo<User> findAllDisabledUsers(Integer page, Integer size);

    Set<Order> findMyOrders(String id);

    void logout(String token);
}

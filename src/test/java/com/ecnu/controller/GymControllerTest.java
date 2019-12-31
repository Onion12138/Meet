package com.ecnu.controller;

import com.alibaba.fastjson.JSON;
import com.ecnu.domain.Gym;
import com.ecnu.domain.User;
import com.ecnu.request.GymFilterRequest;
import com.ecnu.enums.ResultEnum;
import com.ecnu.service.GymService;
import com.ecnu.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author onion
 * @date 2019/12/25 -9:55 下午
 */
@SpringBootTest
@AutoConfigureMockMvc
public class GymControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GymService gymService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private String token;
    private String adminToken;
    @BeforeEach
    public void init(){
        User user = new User();
        User admin = new User();
        user.setEmail("969023014@qq.com");
        user.setNickname("onion");
        user.setAdmin(false);
        token = JwtUtil.createJwt(user);
        admin.setEmail(user.getEmail());
        admin.setNickname(user.getNickname());
        admin.setAdmin(true);
        adminToken = JwtUtil.createJwt(admin);
        redisTemplate.opsForValue().set(token, user.getEmail());
        redisTemplate.opsForValue().set(adminToken, admin.getEmail());
    }

    @Test
    @DisplayName("用户登陆后，以默认分页查看所有场馆")
    public void testFindAllGyms() throws Exception {
        ResultActions perform = mockMvc.perform(get("/gym/allGyms").header("user_token", token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(gymService,times(1)).findAllGyms(anyInt(), anyInt());
    }

    @Test
    @DisplayName("用户未登陆查看场馆抛出异常,要求登陆")
    public void testFindAllGymsWithoutLogin() throws Exception {
        ResultActions perform = mockMvc.perform(get("/gym/allGyms"));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.message").value(ResultEnum.MUST_LOGIN.getMessage()));
    }

    @Test
    @DisplayName("用户根据场馆地址或名字匹配")
    public void testFindGymsByKeyword() throws Exception{
        String keyword = "乒乓球";
        ResultActions perform = mockMvc.perform(get("/gym/keyword").param("keyword", keyword)
                .header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(gymService, times(1)).findGymsByKeyword(anyInt(), anyInt(), anyString());
    }

    @Test
    @DisplayName("用户根据筛选条件查看场馆")
    public void testFindGymByFilter() throws Exception {
        GymFilterRequest gymFilterRequest = new GymFilterRequest();
        gymFilterRequest.setType("乒乓球");
        gymFilterRequest.setAddress("大学生活动中心一楼");
        gymFilterRequest.setHighToLow(true);
        gymFilterRequest.setOpenOnly(true);
        String content = JSON.toJSONString(gymFilterRequest);
        ResultActions perform = mockMvc.perform(post("/gym/filter").contentType(MediaType.APPLICATION_JSON).content(content).header("user_token", token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(gymService).findGymsByFilter(anyInt(), anyInt(), any());
    }

    @Test
    @DisplayName("用户查看场馆评分和评价")
    public void testFindScore() throws Exception {
        ResultActions perform = mockMvc.perform(get("/gym/score").param("gymId", "1576735204701168412")
                .header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(gymService).findScore(anyString());
    }

    @Test
    @DisplayName("用户添加场馆提示权限不够")
    public void testUserAddGym() throws Exception {
        Gym gym = new Gym();
        gym.setAddress("华东师范大学");
        gym.setName("雀坛麻将馆");
        gym.setDescription("雀神争霸");
        gym.setPhoto("http://baidu.com");
        gym.setType("麻将");
        gym.setRent(100.0);
        String content = JSON.toJSONString(gym);
        ResultActions perform = mockMvc.perform(post("/gym/addGym").contentType(MediaType.APPLICATION_JSON).content(content)
                .header("user_token",token));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.message").value( ResultEnum.NO_AUTHORITY.getMessage()));
    }



    @Test
    @DisplayName("管理员添加场馆信息")
    public void testAdminAddGym() throws Exception {
        Gym gym = new Gym();
        gym.setAddress("华东师范大学");
        gym.setName("雀坛麻将馆");
        gym.setDescription("雀神争霸");
        gym.setPhoto("http://baidu.com");
        gym.setType("麻将");
        gym.setRent(100.0);
        String content = JSON.toJSONString(gym);
        ResultActions perform = mockMvc.perform(post("/gym/addGym").contentType(MediaType.APPLICATION_JSON).content(content)
                .header("user_token",adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(gymService).addGym(any());
    }
    @Test
    @DisplayName("管理员更新场馆信息")
    public void testAdminUpdateGym() throws Exception {
        Gym gym = new Gym();
        gym.setGymId("1576735101615918632");
        gym.setType("棋牌");
        gym.setRent(111.1);
        String content = JSON.toJSONString(gym);
        ResultActions perform = mockMvc.perform(post("/gym/updateGym").contentType(MediaType.APPLICATION_JSON).content(content)
                .header("user_token",adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(gymService).updateGym(any());
    }
    @Test
    @DisplayName("管理员删除场馆信息")
    public void testAdminDeleteGym() throws Exception {
        ResultActions perform = mockMvc.perform(post("/gym/deleteGym").contentType(MediaType.APPLICATION_JSON).param("gymId", "1576735101615918632")
                .header("user_token",adminToken));
        perform.andExpect(status().isOk());
        perform.andExpect(jsonPath("$.code").value(0));
        verify(gymService).deleteGym(anyString());
    }

}

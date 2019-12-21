package com.ecnu.service;

import com.ecnu.domain.Gym;
import com.ecnu.dto.GymFilterRequest;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @author onion
 * @date 2019/12/11 -6:24 下午
 */
public interface GymService {
    PageInfo<Gym> findAllGyms(Integer page, Integer size);

    void addGym(Gym gym);

    void updateGym(Gym gym);

    void deleteGym(String gymId);

    PageInfo<Gym> findGymsByKeyword(Integer page, Integer size, String keyword);

    PageInfo<Gym> findGymsByFilter(Integer page, Integer size, GymFilterRequest request);

    Map<String, Object> findScore(String gymId);

//    List<Integer> findAvailableTime(AvailableTimeRequest request);
}

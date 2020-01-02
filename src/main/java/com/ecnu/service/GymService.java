package com.ecnu.service;

import com.ecnu.domain.Gym;
import com.ecnu.request.GymFilterRequest;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * @date 2019/12/11 -6:24 下午
 */
public interface GymService {
    PageInfo<Gym> findAllGyms(Integer page, Integer size);

    Gym addGym(Gym gym);

    Gym updateGym(Gym gym);

    void deleteGym(String gymId);

    PageInfo<Gym> findGymsByKeyword(Integer page, Integer size, String keyword);

    PageInfo<Gym> findGymsByFilter(Integer page, Integer size, GymFilterRequest request);

    Map<String, Object> findScore(String gymId);

//    List<Integer> findAvailableTime(AvailableTimeRequest request);
}

package com.ecnu.service;

import com.ecnu.domain.Gym;
import com.ecnu.domain.GymComment;
import com.ecnu.dto.GymFilterRequest;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/11 -6:24 下午
 */
public interface GymService {
    Map<String, Object> findAllGyms(Integer page, Integer size);

    List<GymComment> findGymComments(String gymId);

    void addGym(Gym gym);

    void updateGym(Gym gym);

    void deleteGym(int gymId);

    void deleteGyms(Set<Integer> idList);

    PageInfo<Gym> findGymsByKeyword(Integer page, Integer size, String keyword);

    PageInfo<Gym> findGymsByFilter(Integer page, Integer size, GymFilterRequest request);
}

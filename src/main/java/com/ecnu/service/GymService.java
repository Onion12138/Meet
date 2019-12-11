package com.ecnu.service;

import com.ecnu.domain.Gym;
import com.ecnu.domain.GymComment;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/11 -6:24 下午
 */
public interface GymService {
    PageInfo<Gym> findAllGyms(Integer page, Integer size);

    List<GymComment> findGymComments(String gymId);

    void addGym(Gym gym);

    void updateGym(Gym gym);

    void deleteGym(String gymId);

    void deleteGyms(Set<String> idList);
}

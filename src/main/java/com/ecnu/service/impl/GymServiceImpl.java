package com.ecnu.service.impl;

import com.ecnu.domain.Gym;
import com.ecnu.domain.GymComment;
import com.ecnu.service.GymService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/11 -9:45 下午
 */
@Service
public class GymServiceImpl implements GymService {
    @Override
    public PageInfo<Gym> findAllGyms(Integer page, Integer size) {
        return null;
    }

    @Override
    public List<GymComment> findGymComments(String gymId) {
        return null;
    }

    @Override
    public void addGym(Gym gym) {

    }

    @Override
    public void updateGym(Gym gym) {

    }

    @Override
    public void deleteGym(String gymId) {

    }

    @Override
    public void deleteGyms(Set<String> idList) {

    }
}

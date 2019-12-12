package com.ecnu.service.impl;

import com.ecnu.dao.GymMapper;
import com.ecnu.domain.Gym;
import com.ecnu.domain.GymComment;
import com.ecnu.dto.GymFilterRequest;
import com.ecnu.service.GymService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

/**
 * @author onion
 * @date 2019/12/11 -9:45 下午
 */
@Service
public class GymServiceImpl implements GymService {
    @Autowired
    private GymMapper gymMapper;

    @Override
    public PageInfo<Gym> findAllGyms(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Gym> gyms = gymMapper.selectAll();
        return new PageInfo<>(gyms);
    }

    @Override
    public PageInfo<Gym> findGymsByKeyword(Integer page, Integer size, String keyword) {
        PageHelper.startPage(page, size);
        Example example = new Example(Gym.class);
        Example.Criteria criteria1 = example.createCriteria();
        Example.Criteria criteria2 = example.createCriteria();
        criteria1.andLike("name", keyword);
        criteria2.andLike("description", keyword);
        example.or(criteria2);
        return new PageInfo<>(gymMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Gym> findGymsByFilter(Integer page, Integer size, GymFilterRequest request) {
        PageHelper.startPage(page, size);
        Example example = new Example(Gym.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(request.getName())) {
            criteria.andEqualTo("name", request.getName());
        }
        if (!StringUtils.isEmpty(request.getAddress())){
            criteria.andEqualTo("address", request.getAddress());
        }
        if (request.getOpenOnly()){
            criteria.andEqualTo("open", true);
        }
        if (request.getHighToLow()){
            example.orderBy("rent").desc();
        } else{
            example.orderBy("rent").asc();
        }
        example.and(criteria);
        List<Gym> gyms = gymMapper.selectByExample(example);
        return new PageInfo<>(gyms);
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

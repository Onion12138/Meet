package com.ecnu.service.impl;

import com.ecnu.dao.GymDao;
import com.ecnu.dao.GymMapper;
import com.ecnu.domain.Gym;
import com.ecnu.domain.Order;
import com.ecnu.request.GymFilterRequest;
import com.ecnu.service.GymService;
import com.ecnu.utils.KeyUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author onion
 * @date 2019/12/11 -9:45 下午
 */
@Service
public class GymServiceImpl implements GymService {
    @Autowired
    private GymMapper gymMapper;
    @Autowired
    private GymDao gymDao;

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
        criteria1.andLike("name", "%" + keyword + "%");
        criteria2.andLike("description", "%" + keyword + "%");
        example.or(criteria2);
        return new PageInfo<>(gymMapper.selectByExample(example));
    }

    //查看mysql的语句
    @Override
    public PageInfo<Gym> findGymsByFilter(Integer page, Integer size, GymFilterRequest request) {
        PageHelper.startPage(page, size);
        Example example = new Example(Gym.class);
        Example.Criteria criteria = example.createCriteria();
        if (!StringUtils.isEmpty(request.getType())) {
            criteria.andEqualTo("type", request.getType());
        }
        if (!StringUtils.isEmpty(request.getAddress())){
            criteria.andLike("address", request.getAddress() + "%");
        }
        if (request.getOpenOnly()){
            criteria.andEqualTo("open", true);
        }
        if (request.getHighToLow()){
            example.orderBy("rent").desc();
        } else{
            example.orderBy("rent").asc();
        }
        List<Gym> gyms = gymMapper.selectByExample(example);
        return new PageInfo<>(gyms);
    }

    @Override
    public Map<String, Object> findScore(String gymId) {
        Gym gym = gymDao.findById(gymId).get();
        Set<Order> orderSet = gym.getOrderSet();
        Map<String, Object> map = new HashMap<>();
        if (orderSet == null) {
            map.put("score", 0);
            map.put("comment", "暂无订单和评论");
            return map;
        }

        Optional<Integer> sum = orderSet.stream().map(Order::getScore).reduce(Integer::sum);
        int sumValue = sum.orElse(1);

        List<String> comments = orderSet.stream().map(Order::getComment).collect(Collectors.toList());
        map.put("score", sumValue / (double) orderSet.size());
        map.put("comment", comments);
        return map;
    }

    @Override
    public Gym addGym(Gym gym) {
        gym.setGymId(KeyUtil.genUniqueKey());
        gym.setOpen(true);
        gymMapper.insert(gym);
        return gym;
    }

    @Override
    public Gym updateGym(Gym gym) {
        gymMapper.updateByPrimaryKeySelective(gym);
        return gymMapper.selectOne(gym);
    }

    @Override
    public void deleteGym(String gymId) {
        Gym gym = new Gym();
        gym.setGymId(gymId);
        gym.setOpen(false);
        gymMapper.updateByPrimaryKeySelective(gym);
    }

}

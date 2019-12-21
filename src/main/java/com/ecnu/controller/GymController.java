package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.domain.Gym;
import com.ecnu.dto.GymFilterRequest;
import com.ecnu.service.GymService;
import com.ecnu.vo.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author onion
 * @date 2019/12/11 -9:35 上午
 */
@RestController
@RequestMapping("/gym")
public class GymController {
    @Autowired
    private GymService gymService;
    @GetMapping("/allGyms")
    public ResultEntity findAllGyms(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        PageInfo<Gym> gymPageInfo = gymService.findAllGyms(page, size);
        return ResultEntity.succeed(gymPageInfo);
    }

    @GetMapping("/keyword")
    public ResultEntity findGymsByKeyword(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size, @RequestParam String keyword){
        PageInfo<Gym> gyms = gymService.findGymsByKeyword(page, size, keyword);
        return ResultEntity.succeed(gyms);
    }

    @GetMapping("/filter")
    public ResultEntity findGymByFilter(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size, @RequestBody GymFilterRequest request){
        PageInfo<Gym> gyms = gymService.findGymsByFilter(page, size, request);
        return ResultEntity.succeed(gyms);
    }
    @GetMapping("/score")
    public ResultEntity findScore(@RequestParam String gymId){
        Map<String, Object> map = gymService.findScore(gymId);
        return ResultEntity.succeed(map);
    }

    @PostMapping("/addGym")
    @AdminOnly
    public ResultEntity addGym(@RequestBody Gym gym){
        gymService.addGym(gym);
        return ResultEntity.succeed();
    }

    @PostMapping("/updateGym")
    @AdminOnly
    public ResultEntity updateGym(@RequestBody Gym gym){
        gymService.updateGym(gym);
        return ResultEntity.succeed();
    }

    @PostMapping("/deleteGym")
    @AdminOnly
    public ResultEntity deleteGym(@RequestParam String gymId){
        gymService.deleteGym(gymId);
        return ResultEntity.succeed();
    }

}

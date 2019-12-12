package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.domain.Gym;
import com.ecnu.domain.GymComment;
import com.ecnu.dto.GymFilterRequest;
import com.ecnu.service.GymService;
import com.ecnu.vo.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public ResultEntity findAllGyms(@RequestParam Integer page, @RequestParam Integer size){
        Map<String, Object> map = gymService.findAllGyms(page, size);
        return ResultEntity.succeed(map);
    }

    @GetMapping("/keyword")
    public ResultEntity findGymsByKeyword(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String keyword){
        PageInfo<Gym> gyms = gymService.findGymsByKeyword(page, size, keyword);
        return ResultEntity.succeed(gyms);
    }

    @GetMapping("/filter")
    public ResultEntity findGymByFilter(@RequestParam Integer page, @RequestParam Integer size, @RequestBody GymFilterRequest request){
        PageInfo<Gym> gyms = gymService.findGymsByFilter(page, size, request);
        return ResultEntity.succeed(gyms);
    }

    @GetMapping("/comments")
    public ResultEntity findGymComments(@RequestParam String gymId){
        List<GymComment> gymComments = gymService.findGymComments(gymId);
        return ResultEntity.succeed(gymComments);
    }

    @PutMapping("/addGym")
    @AdminOnly
    public ResultEntity addGym(@RequestBody Gym gym){
        gymService.addGym(gym);
        return ResultEntity.succeed();
    }

    @PostMapping("/updateGyms")
    @AdminOnly
    public ResultEntity updateGym(@RequestParam Gym gym){
        gymService.updateGym(gym);
        return ResultEntity.succeed();
    }

    @DeleteMapping("/deleteGym")
    @AdminOnly
    public ResultEntity deleteGym(@RequestParam String gymId){
        gymService.deleteGym(gymId);
        return ResultEntity.succeed();
    }

    @DeleteMapping("/deleteGyms")
    @AdminOnly
    public ResultEntity deleteGyms(@RequestParam Set<String> idList){
        gymService.deleteGyms(idList);
        return ResultEntity.succeed();
    }
}

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
    /*
    * 查看所有的场馆
    * */
    @GetMapping("/allGyms")
    public ResultEntity findAllGyms(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        PageInfo<Gym> gymPageInfo = gymService.findAllGyms(page, size);
        return ResultEntity.succeed(gymPageInfo);
    }
    /*
    * 通过关键字查看，模糊匹配场馆的地址和名字
    * */
    @GetMapping("/keyword")
    public ResultEntity findGymsByKeyword(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size, @RequestParam String keyword){
        PageInfo<Gym> gyms = gymService.findGymsByKeyword(page, size, keyword);
        return ResultEntity.succeed(gyms);
    }

    /*
    * 通过筛选器查找，封装成GymFilterRequest对象
    * String type 可以为空，对应到过滤条件即为默认的不限;
    * String address 可以为空，对应到过滤条件即为默认的不限;
    * type和address可以写死在前端页面
    * Boolean highToLow 租金价格从高到低排序还是从低到高排序，不能为空，一定要有默认值，比如默认从低到高排序;
    * Boolean openOnly 意思为查询全部还是查询开放的场馆。（有的场馆可能被关闭了，open值为false）不能为空，一定有默认值，比如默认为查询全部;
    * */
    @PostMapping("/filter")
    public ResultEntity findGymByFilter(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size, @RequestBody GymFilterRequest request){
        PageInfo<Gym> gyms = gymService.findGymsByFilter(page, size, request);
        return ResultEntity.succeed(gyms);
    }
    /*
    * 查看此场馆的评分和评价，返回一个map，里面有如下信息
    * score：评分，double类型，可能需要保留2位
    * comment：评价内容，字符串类型
    * */
    @GetMapping("/score")
    public ResultEntity findScore(@RequestParam String gymId){
        Map<String, Object> map = gymService.findScore(gymId);
        return ResultEntity.succeed(map);
    }

    /*
    我没有单独封装为xxxRequest对象，和原类型差不多
    private String gymId; //不需要提供，后端自动生成
    private String name;
    private String description;
    private String address;
    private Double rent; //租金价格
    private Boolean open; //是否开放，可以不提供，默认为true
    private String photo; //图片url
    private String type; //type类型有规定，最好
    * */
    @PostMapping("/addGym")
    @AdminOnly
    public ResultEntity addGym(@RequestBody Gym gym){
        gymService.addGym(gym);
        return ResultEntity.succeed();
    }
    /*
    * 更新场馆信息
    * 传入部分字段就可以，其他不需要的字段可以不提供，但一定要传入主键
    * */
    @PostMapping("/updateGym")
    @AdminOnly
    public ResultEntity updateGym(@RequestBody Gym gym){
        gymService.updateGym(gym);
        return ResultEntity.succeed();
    }
    /*
    * 逻辑删除，把open置为false
    * */
    @PostMapping("/deleteGym")
    @AdminOnly
    public ResultEntity deleteGym(@RequestParam String gymId){
        gymService.deleteGym(gymId);
        return ResultEntity.succeed();
    }

}

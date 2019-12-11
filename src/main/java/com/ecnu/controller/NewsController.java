package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.domain.News;
import com.ecnu.dto.CommentRequest;
import com.ecnu.dto.NewsRequest;
import com.ecnu.service.NewsService;
import com.ecnu.vo.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author onion
 * @date 2019/12/11 -9:41 上午
 */
@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;
    @GetMapping("/all")
    public ResultEntity findAllNews(@RequestParam Integer page, @RequestParam Integer size){
        PageInfo<News> newsPageInfo = newsService.findAllNews(page, size);
        return ResultEntity.succeed(newsPageInfo);
    }
    @GetMapping("/today")
    public ResultEntity findTodayNews(@RequestParam Integer page, @RequestParam Integer size){
        PageInfo<News> newsPageInfo = newsService.findTodayNews(page, size);
        return ResultEntity.succeed(newsPageInfo);
    }
    @PostMapping("/addComment")
    public ResultEntity addComment(@RequestBody CommentRequest request){
        newsService.addComment(request);
        return ResultEntity.succeed();
    }
    @DeleteMapping("/deleteComment")
    public ResultEntity deleteComment(@RequestParam String commentId){
        newsService.deleteComment(commentId);
        return ResultEntity.succeed();
    }
    @PostMapping("/addNews")
    @AdminOnly
    public ResultEntity addNews(@Validated @RequestBody NewsRequest newsRequest){
        newsService.addNews(newsRequest);
        return ResultEntity.succeed();
    }
    @PutMapping("/updateNews")
    @AdminOnly
    public ResultEntity updateNews(@Validated @RequestBody NewsRequest newsRequest){
        newsService.updateNews(newsRequest);
        return ResultEntity.succeed();
    }
    @DeleteMapping("/deleteNews")
    @AdminOnly
    public ResultEntity deleteNews(@RequestParam String newsId){
        newsService.deleteNews(newsId);
        return ResultEntity.succeed();
    }
}

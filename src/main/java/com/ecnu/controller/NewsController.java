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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

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
    public ResultEntity findAllNews(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        PageInfo<News> newsPageInfo = newsService.findAllNews(page, size);
        return ResultEntity.succeed(newsPageInfo);
    }
    @GetMapping("/today")
    public ResultEntity findTodayNews(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        PageInfo<News> newsPageInfo = newsService.findTodayNews(page, size);
        return ResultEntity.succeed(newsPageInfo);
    }
    @GetMapping("/one")
    public ResultEntity findOneNews(@RequestParam String newsId){
        News news = newsService.findOneNews(newsId);
        return ResultEntity.succeed(news);
    }
    @PostMapping("/addComment")
    public ResultEntity addComment(@RequestBody CommentRequest commentRequest){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
        newsService.addComment(commentRequest, token);
        return ResultEntity.succeed();
    }
    @PostMapping("/deleteComment")
    public ResultEntity deleteComment(@RequestParam String commentId){
        newsService.deleteComment(commentId);
        return ResultEntity.succeed();
    }
    @PostMapping("/addNews")
    @AdminOnly
    public ResultEntity addNews(@Validated @RequestBody NewsRequest newsRequest){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
        newsService.addNews(newsRequest, token);
        return ResultEntity.succeed();
    }
    @PostMapping("/updateNews")
    @AdminOnly
    public ResultEntity updateNews(@Validated @RequestBody NewsRequest newsRequest){
        newsService.updateNews(newsRequest);
        return ResultEntity.succeed();
    }
    @PostMapping("/deleteNews")
    @AdminOnly
    public ResultEntity deleteNews(@RequestParam String newsId){
        newsService.deleteNews(newsId);
        return ResultEntity.succeed();
    }
}

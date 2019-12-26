package com.ecnu.controller;

import com.ecnu.annotation.AdminOnly;
import com.ecnu.domain.News;
import com.ecnu.dto.CommentRequest;
import com.ecnu.dto.NewsRequest;
import com.ecnu.service.NewsService;
import com.ecnu.utils.ParamUtil;
import com.ecnu.vo.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author onion
 * @date 2019/12/11 -9:41 上午
 */
@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;
    /*
    * 查看所有的news，不会返回news有关的NewsComment
    * */
    @GetMapping("/all")
    public ResultEntity findAllNews(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        PageInfo<News> newsPageInfo = newsService.findAllNews(page, size);
        return ResultEntity.succeed(newsPageInfo);
    }
    /*
    * 查看今天的news，不会返回NewsComment
    * */
    @GetMapping("/today")
    public ResultEntity findTodayNews(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "5") Integer size){
        PageInfo<News> newsPageInfo = newsService.findTodayNews(page, size);
        return ResultEntity.succeed(newsPageInfo);
    }
    /*
    * 查看某个news，会返回相关的评论
    * 进阶：评论的树形结构
    * 需要前端根据所需要的数据来确定返回的结构
    * 目前这个API只是简单返回News，News里面有Comment的集合
    * */
    @GetMapping("/one")
    public ResultEntity findOneNews(@RequestParam String newsId){
        News news = newsService.findOneNews(newsId);
        return ResultEntity.succeed(news);
    }
    /*
     *  添加评论，如下
     *  @NotEmpty
        private String newsId;
        private String parentId;
        @NotEmpty
        private String content;
     *
     * 参数校验失败，为什么？
     * */
    @PostMapping("/addComment")
    public ResultEntity addComment(@RequestBody @Valid CommentRequest commentRequest, BindingResult result){
        ParamUtil.verifyParam(result);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
        newsService.addComment(commentRequest, token);
        return ResultEntity.succeed();
    }
    /*
    * 没有提供修改评论的接口，类似微信，只能删除和添加
    * */
    @PostMapping("/deleteComment")
    public ResultEntity deleteComment(@RequestParam String commentId){
        newsService.deleteComment(commentId);
        return ResultEntity.succeed();
    }
    /*
    * 还没有提供上传图片的接口。暂且就添加网络图片吧？
    * NewsRequest对象
    * 添加news的时候不需要提供newId，更新的时候需要
    * content可以存html
    * private String newsId;
      @NotEmpty
      private String content;
      @NotEmpty
      private String title;
    * */
    @PostMapping("/addNews")
    @AdminOnly
    public ResultEntity addNews(@Validated @RequestBody NewsRequest newsRequest, BindingResult result){
        ParamUtil.verifyParam(result);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("user_token");
        newsService.addNews(newsRequest, token);
        return ResultEntity.succeed();
    }
    /*
    * 见上一条
    * */
    @PostMapping("/updateNews")
    @AdminOnly
    public ResultEntity updateNews(@Validated @RequestBody NewsRequest newsRequest, BindingResult result){
        ParamUtil.verifyParam(result);
        newsService.updateNews(newsRequest);
        return ResultEntity.succeed();
    }
    /*
    * 慎用！会导致级联删除
    * 目前测试此借口会报外键约束的错误，还没有解决
    * 暂且不要使用这个接口
    * */
    @PostMapping("/deleteNews")
    @AdminOnly
    public ResultEntity deleteNews(@RequestParam String newsId){
        newsService.deleteNews(newsId);
        return ResultEntity.succeed();
    }
}

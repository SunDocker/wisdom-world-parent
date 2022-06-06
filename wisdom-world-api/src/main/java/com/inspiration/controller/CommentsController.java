package com.inspiration.controller;

import com.inspiration.service.CommentsService;
import com.inspiration.vo.Result;
import com.inspiration.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author SunDocker
 */
@RestController
@RequestMapping("comments")
public class CommentsController {
    @Autowired
    CommentsService commentsService;

    @GetMapping("idea/{id}")
    public Result getIdeaComments(@PathVariable("id") Long ideaId) {
        return commentsService.getIdeaCommentsByIdeaId(ideaId);
    }

    @PostMapping("publish")
    public Result comment(@RequestBody CommentParam commentParam) {
        return commentsService.comment(commentParam);
    }
}

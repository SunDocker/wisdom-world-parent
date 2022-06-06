package com.inspiration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspiration.dao.pojo.Comment;
import com.inspiration.vo.Result;
import com.inspiration.vo.params.CommentParam;

/**
 * @author SunDocker
 */
public interface CommentsService extends IService<Comment> {
    /**
     * 通过ideaId获得评论
     * @param ideaId
     * @return
     */
    Result getIdeaCommentsByIdeaId(Long ideaId);

    /**
     * 评论
     * @param commentParam
     * @return
     */
    Result comment(CommentParam commentParam);
}

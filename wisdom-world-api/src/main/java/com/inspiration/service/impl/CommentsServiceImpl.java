package com.inspiration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspiration.dao.mapper.CommentsMapper;
import com.inspiration.dao.pojo.Comment;
import com.inspiration.dao.pojo.Idea;
import com.inspiration.dao.pojo.SysUser;
import com.inspiration.service.CommentsService;
import com.inspiration.service.IdeaService;
import com.inspiration.service.SysUserService;
import com.inspiration.service.ThreadService;
import com.inspiration.vo.CommentVo;
import com.inspiration.vo.LoginUser;
import com.inspiration.vo.Result;
import com.inspiration.vo.UserVo;
import com.inspiration.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SunDocker
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comment> implements CommentsService {
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private IdeaService ideaService;

    @Override
    public Result getIdeaCommentsByIdeaId(Long ideaId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getIdeaId, ideaId);
        queryWrapper.eq(Comment::getLevel, 1);
        queryWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentsMapper.selectList(queryWrapper);
        List<CommentVo> commentVoList = copyList(comments);
        return Result.success(commentVoList);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SysUser sysUser = loginUser.getSysUser();
        Comment comment = new Comment();
        comment.setIdeaId(commentParam.getIdeaId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parentId = commentParam.getParentId();
        if (parentId == null || parentId == 0) {
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parentId == null ? 0 : parentId);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentsMapper.insert(comment);

        CommentVo commentVo = copy(comment);

        Idea ideaForUpdate = ideaService.getById(commentParam.getIdeaId());
        threadService.increaseIdeaCommentCounts(ideaForUpdate);

        return Result.success(commentVo);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVoList = new ArrayList<>();
        for (Comment comment : comments) {
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();
        BeanUtils.copyProperties(comment, commentVo);
        //评论作者
        SysUser authorSysUser = sysUserService.getById(comment.getAuthorId());
        UserVo authorUserVo = new UserVo();
        BeanUtils.copyProperties(authorSysUser, authorUserVo);
        commentVo.setAuthor(authorUserVo);
        //子评论
        int level = comment.getLevel();
        if (1 == level) {
            Long parentId = comment.getId();
            List<CommentVo> commentVoList = findCommentsByParentId(parentId);
            commentVo.setChildren(commentVoList);
        }
        //评论的父评论作者信息
        if (1 < level) {
            SysUser toSysUser = sysUserService.getById(comment.getToUid());
            UserVo toUserVo = new UserVo();
            BeanUtils.copyProperties(toSysUser, toUserVo);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    private List<CommentVo> findCommentsByParentId(Long parentId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId, parentId);
        queryWrapper.eq(Comment::getLevel, 2);
        return copyList(commentsMapper.selectList(queryWrapper));
    }
}

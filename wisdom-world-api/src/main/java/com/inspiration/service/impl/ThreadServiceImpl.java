package com.inspiration.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.inspiration.dao.mapper.IdeaMapper;
import com.inspiration.dao.pojo.Idea;
import com.inspiration.service.IdeaService;
import com.inspiration.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author SunDocker
 */
@Service
public class ThreadServiceImpl implements ThreadService {
    @Autowired
    private IdeaMapper ideaMapper;

    @Override
    public void increaseIdeaViewCounts(Idea idea) {
        Integer viewCounts = idea.getViewCounts();
        Idea ideaUpdate = new Idea();
        ideaUpdate.setViewCounts(viewCounts + 1);
        LambdaUpdateWrapper<Idea> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Idea::getId, idea.getId());
        //乐观锁
        updateWrapper.eq(Idea::getViewCounts, viewCounts);
        ideaMapper.update(ideaUpdate, updateWrapper);
    }

    @Override
    public void increaseIdeaCommentCounts(Idea idea) {
        Integer commentCounts = idea.getCommentCounts();
        Idea ideaUpdate = new Idea();
        ideaUpdate.setCommentCounts(commentCounts + 1);
        LambdaUpdateWrapper<Idea> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Idea::getId, idea.getId());
        //乐观锁
        updateWrapper.eq(Idea::getCommentCounts, commentCounts);
        ideaMapper.update(ideaUpdate, updateWrapper);
    }
}

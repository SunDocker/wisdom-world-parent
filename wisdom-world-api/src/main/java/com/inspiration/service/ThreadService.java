package com.inspiration.service;

import com.inspiration.dao.pojo.Idea;
import org.springframework.scheduling.annotation.Async;

/**
 * @author SunDocker
 */

public interface ThreadService {


    /**
     * idea阅读量++
     * @param idea
     */
    @Async("asyncServiceExecutor")
    void increaseIdeaViewCounts(Idea idea);

    /**
     * idea评论量++
     * @param idea
     */
    @Async("asyncServiceExecutor")
    void increaseIdeaCommentCounts(Idea idea);
}

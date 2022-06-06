package com.inspiration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspiration.dao.pojo.Idea;
import com.inspiration.vo.Result;
import com.inspiration.vo.params.IdeaBodyParam;
import com.inspiration.vo.params.IdeaParam;
import com.inspiration.vo.params.PageParams;

/**
 * @author SunDocker
 */
public interface IdeaService extends IService<Idea> {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listIdeas(PageParams pageParams);

    /**
     * 最热ideas
     * @param limit
     * @return
     */
    Result hotIdeas(int limit);

    /**
     * 最新ideas
     * @param limit
     * @return
     */
    Result latestIdeas(int limit);

    /**
     * idea归档
     * @return
     */
    Result listArchives();

    /**
     * 查看idea详情
     * @param ideaId
     * @return
     */
    Result findIdeaWithBodyById(Long ideaId);

    /**
     * 发布idea
     * @param ideaParam
     * @return
     */
    Result publish(IdeaParam ideaParam);

    /**
     * 更新idea
     * @return
     */
    Result update(IdeaParam ideaParam);

    /**
     * 获得idea树
     * @param id
     * @return
     */
    Result getIdeaTree(Long id);

    /**
     * 根据输入框内容寻找idea
     * @param search
     * @return
     */
    Result search(String search);

    /**
     * 通过作者id查询所有ideas
     * @param authorId
     * @return
     */
    Result getIdeasByAuthorId(Long authorId);

    /**
     * 通过用户id查询收藏
     * @param id
     * @return
     */
    Result listIdeaCollectionByUserId(Long id);

    /**
     * 通过id判断用户是否收藏idea
     * @param uid
     * @param iid
     * @return
     */
    Result isCollected(Long uid, Long iid);
}

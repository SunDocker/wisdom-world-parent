package com.inspiration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.inspiration.dao.pojo.Tag;
import com.inspiration.vo.Result;
import com.inspiration.vo.TagVo;

import java.util.List;

/**
 * @author SunDocker
 */
public interface TagService extends IService<Tag> {

    /**
     * 通过想法id获取标签
     * @param ideaId
     * @return
     */
    List<TagVo> findTagsByIdeaId(Long ideaId);

    /**
     * 返回最热标签，不超过limit个
     * @param limit
     * @return
     */
    Result hots(Integer limit);

    /**
     * 获取所有标签
     * @return
     */
    Result allTags();

    /**
     * 获取所有标签的详细信息
     * @return
     */
    Result allTagsDetail();

    /**
     * 通过id获取标签详细信息
     * @param tagId
     * @return
     */
    Result tagDetailById(Long tagId);
}

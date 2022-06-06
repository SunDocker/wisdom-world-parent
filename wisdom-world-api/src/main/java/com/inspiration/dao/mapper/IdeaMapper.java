package com.inspiration.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.inspiration.dao.dos.Archives;
import com.inspiration.dao.pojo.Idea;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author SunDocker
 */
@Repository
public interface IdeaMapper extends BaseMapper<Idea> {
    /**
     * idea归档
     * @return
     */
    List<Archives> listArchives();

    /**
     * 按条件查询idea
     * @param page
     * @param categoryId
     * @param tagId
     * @param year
     * @param month
     * @return
     */
    Page<Idea> listIdeas(Page<Idea> page, Long categoryId, Long tagId, String year, String month);

    /**
     * 根据输入查询匹配标题的idea
     * @param search
     * @return
     */
    List<Idea> searchIdeasByTitle(String search);
}

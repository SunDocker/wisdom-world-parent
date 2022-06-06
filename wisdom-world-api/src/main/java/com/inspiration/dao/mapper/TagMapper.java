package com.inspiration.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.inspiration.dao.pojo.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author SunDocker
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据想法id查询标签列表
     * @param ideaId
     * @return
     */
    List<Tag> findTagsByIdeaId(Long ideaId);

    /**
     * 查询limit个最热标签
     * @param limit
     */
    List<Long> findHotTagIds(Integer limit);
}

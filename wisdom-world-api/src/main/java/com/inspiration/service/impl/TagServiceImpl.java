package com.inspiration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspiration.dao.mapper.TagMapper;
import com.inspiration.dao.pojo.Idea;
import com.inspiration.dao.pojo.Tag;
import com.inspiration.service.TagService;
import com.inspiration.vo.Result;
import com.inspiration.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author SunDocker
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
    @Autowired
    private TagMapper tagMapper;

    @Override
    public List<TagVo> findTagsByIdeaId(Long ideaId) {
        List<Tag> tags = tagMapper.findTagsByIdeaId(ideaId);
        return copyList(tags);
    }

    @Override
    public Result hots(Integer limit) {
        List<Long> hotTagIds = tagMapper.findHotTagIds(limit);
        List<Tag> tags = listByIds(hotTagIds);
        return Result.success(tags);
    }

    @Override
    public Result allTags() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId, Tag::getTagName);
        return Result.success(copyList(list(queryWrapper)));
    }

    @Override
    public Result allTagsDetail() {
        return Result.success(copyList(list()));
    }

    @Override
    public Result tagDetailById(Long tagId) {
        return Result.success(copy(getById(tagId)));
    }

    private List<TagVo> copyList(List<Tag> tags) {
        return tags.stream().map(this::copy).collect(Collectors.toList());
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag, tagVo);
        return tagVo;
    }
}

package com.inspiration.controller;

import com.inspiration.service.TagService;
import com.inspiration.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SunDocker
 */
@RestController
@RequestMapping("tags")
public class TagsController {
    @Autowired
    TagService tagService;

    @GetMapping
    public Result tags() {
        return tagService.allTags();
    }

    @GetMapping("hot")
    public Result hots() {
        int limit = 6;
        return tagService.hots(limit);
    }

    @GetMapping("detail")
    public Result tagsDetail() {
        return tagService.allTagsDetail();
    }

    @GetMapping("detail/{id}")
    public Result tagsDetail(@PathVariable("id") Long tagId) {
        return tagService.tagDetailById(tagId);
    }
}

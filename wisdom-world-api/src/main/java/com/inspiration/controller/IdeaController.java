package com.inspiration.controller;

import com.inspiration.common.aop.LogAnnotation;
import com.inspiration.service.IdeaService;
import com.inspiration.vo.Result;
import com.inspiration.vo.params.IdeaBodyParam;
import com.inspiration.vo.params.IdeaParam;
import com.inspiration.vo.params.PageParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author SunDocker
 */
@RestController
@RequestMapping("ideas")
@Slf4j
public class IdeaController {
    @Autowired
    IdeaService ideaService;

    @PostMapping
    @LogAnnotation(module = "Idea", operator = "获取idea列表")
    public Result listIdeas(@RequestBody PageParams pageParams) {
        return ideaService.listIdeas(pageParams);
    }

    @GetMapping("hot")
    public Result hotIdeas() {
        int limit = 5;
        return ideaService.hotIdeas(limit);
    }

    @GetMapping("latest")
    public Result latestIdeas() {
        int limit = 5;
        return ideaService.latestIdeas(limit);
    }

    @GetMapping("listArchives")
    public Result listArchives() {
        return ideaService.listArchives();
    }

    @GetMapping("view/{id}")
    public Result findIdeaWithBodyById(@PathVariable("id") Long ideaId) {
        return ideaService.findIdeaWithBodyById(ideaId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody IdeaParam ideaParam) {
        return ideaService.publish(ideaParam);
    }

    @PutMapping("update")
    public Result update(@RequestBody IdeaParam ideaParam) {
        return ideaService.update(ideaParam);
    }

    @GetMapping("tree/{id}")
    public Result tree(@PathVariable Long id) {
        return ideaService.getIdeaTree(id);
    }

    @GetMapping("search")
    public Result search(String search) {
        log.info(search);
        return ideaService.search(search);
    }

    @GetMapping("author/{id}")
    public Result getIdeasByAuthorId(@PathVariable Long id) {
        return ideaService.getIdeasByAuthorId(id);
    }

    @GetMapping("collection/{id}")
    public Result listIdeaCollectionByAuthorId(@PathVariable Long id) {
        return ideaService.listIdeaCollectionByUserId(id);
    }

    @GetMapping("isCollected")
    public Result isCollected(Long uid, Long iid) {
        return ideaService.isCollected(uid, iid);
    }
}

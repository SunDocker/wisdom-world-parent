package com.inspiration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.inspiration.dao.dos.Archives;
import com.inspiration.dao.mapper.IdeaBodyMapper;
import com.inspiration.dao.mapper.IdeaMapper;
import com.inspiration.dao.mapper.IdeaTagMapper;
import com.inspiration.dao.pojo.*;
import com.inspiration.service.*;
import com.inspiration.vo.*;
import com.inspiration.vo.params.IdeaBodyParam;
import com.inspiration.vo.params.IdeaParam;
import com.inspiration.vo.params.PageParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SunDocker
 */
@Service
@Transactional
public class IdeaServiceImpl extends ServiceImpl<IdeaMapper, Idea> implements IdeaService {
    @Autowired
    private IdeaMapper ideaMapper;
    @Autowired
    private IdeaBodyMapper ideaBodyMapper;
    @Autowired
    private TagService tagService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private IdeaTagMapper ideaTagMapper;
    @Autowired
    private IdeaCollectionService ideaCollectionService;

    @Override
    public Result listIdeas(PageParams pageParams) {
        Page<Idea> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());

        /*LambdaQueryWrapper<Idea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Idea::getWeight, Idea::getCreateDate);
        if (pageParams.getCategoryId() != null) {
            queryWrapper.eq(Idea::getCategoryId, pageParams.getCategoryId());
        }

        List<Long> ideasIdForTag = null;
        if (pageParams.getTagId() != null) {
            LambdaQueryWrapper<IdeaTag> ideaTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
            ideaTagLambdaQueryWrapper.eq(IdeaTag::getTagId, pageParams.getTagId());
            List<IdeaTag> ideaTags = ideaTagMapper.selectList(ideaTagLambdaQueryWrapper);
            ideasIdForTag = ideaTags.stream().map(IdeaTag::getIdeaId).collect(Collectors.toList());
        }
        if (ideasIdForTag != null) {
            queryWrapper.in(Idea::getId, ideasIdForTag);
        }*/

        Page<Idea> ideaPage = ideaMapper.listIdeas(page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());

        List<Idea> records = ideaPage.getRecords();
        List<IdeaVo> ideaVoList = copyList(records);
        return Result.success(ideaVoList);
    }

    @Override
    public Result hotIdeas(int limit) {
        LambdaQueryWrapper<Idea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Idea::getViewCounts);
        queryWrapper.select(Idea::getId, Idea::getTitle);
        queryWrapper.last("limit " + limit);
        List<Idea> ideas = ideaMapper.selectList(queryWrapper);
        return Result.success(copyList(ideas));
    }

    @Override
    public Result latestIdeas(int limit) {
        LambdaQueryWrapper<Idea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Idea::getCreateDate);
        queryWrapper.select(Idea::getId, Idea::getTitle);
        queryWrapper.last("limit " + limit);
        List<Idea> ideas = ideaMapper.selectList(queryWrapper);
        return Result.success(copyList(ideas));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = ideaMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findIdeaWithBodyById(Long ideaId) {
        Idea idea = ideaMapper.selectById(ideaId);
        IdeaVo ideaVo = copy(idea, true, true, true, true);
        threadService.increaseIdeaViewCounts(idea);
        return Result.success(ideaVo);
    }

    @Override
    public Result publish(IdeaParam ideaParam) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        SysUser sysUser = loginUser.getSysUser();

        if (ideaParam.getTitle() == null) {
            return Result.fail(20005, "idea必须有title");
        }

        Idea idea = new Idea();
        idea.setAuthorId(sysUser.getId());
        idea.setWeight(Idea.IDEA_COMMON);
        idea.setViewCounts(0);
        idea.setTitle(ideaParam.getTitle());
        idea.setSummary(ideaParam.getSummary());
        idea.setCommentCounts(0);
        idea.setCreateDate(System.currentTimeMillis());
        idea.setCategoryId(ideaParam.getCategory().getId());
        Long parentId = ideaParam.getParentId();
        if (Objects.nonNull(parentId)) {
            idea.setParentId(parentId);
        }
        ideaMapper.insert(idea);

        List<TagVo> tags = ideaParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                Long ideaId = idea.getId();
                IdeaTag ideaTag = new IdeaTag();
                ideaTag.setIdeaId(ideaId);
                ideaTag.setTagId(tag.getId());
                ideaTagMapper.insert(ideaTag);
            }
        }

        IdeaBody ideaBody = new IdeaBody();
        ideaBody.setIdeaId(idea.getId());
        ideaBody.setContent(ideaParam.getBody().getContent());
        ideaBody.setContentHtml(ideaParam.getBody().getContentHtml());
        ideaBodyMapper.insert(ideaBody);

        idea.setBodyId(ideaBody.getId());
        ideaMapper.updateById(idea);

        Map<String, String> map = new HashMap<>(1);
        map.put("id", idea.getId().toString());
        return Result.success(map);
    }

    @Override
    public Result update(IdeaParam ideaParam) {
        System.out.println("=================================>" + ideaParam);
        LambdaQueryWrapper<Idea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Idea::getBodyId).eq(Idea::getId, ideaParam.getId());
        Idea ideaToUpdate = getOne(queryWrapper);

        IdeaBody ideaBody = new IdeaBody();
        ideaBody.setId(ideaToUpdate.getBodyId());
        ideaBody.setContent(ideaParam.getBody().getContent());
        ideaBody.setContentHtml(ideaParam.getBody().getContentHtml());
        if (ideaBodyMapper.updateById(ideaBody) != 1) {
            throw new RuntimeException("更新错误");
        }

        Idea ideaForUpdate = new Idea();
        ideaForUpdate.setId(ideaParam.getId());
        ideaForUpdate.setSummary(ideaParam.getSummary());
        ideaForUpdate.setTitle(ideaParam.getTitle());
        ideaForUpdate.setCategoryId(ideaParam.getCategory().getId());
        if (ideaMapper.updateById(ideaForUpdate) != 1) {
            throw new RuntimeException("更新错误");
        }

        List<TagVo> tags = ideaParam.getTags();
        if (tags != null) {
            LambdaQueryWrapper<IdeaTag> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.eq(IdeaTag::getIdeaId, ideaParam.getId());
            ideaTagMapper.delete(deleteWrapper);
            for (TagVo tag : tags) {
                Long ideaId = ideaParam.getId();
                IdeaTag ideaTag = new IdeaTag();
                ideaTag.setIdeaId(ideaId);
                ideaTag.setTagId(tag.getId());
                ideaTagMapper.insert(ideaTag);
            }
        }

        return Result.success(null);
    }

    @Override
    public Result getIdeaTree(Long id) {
        LambdaQueryWrapper<Idea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Idea::getId, Idea::getParentId, Idea::getTitle).eq(Idea::getId, id);
        Idea curIdea = getOne(queryWrapper);
        Long rootId = curIdea.getParentId();
        /*if (rootId == -1) {
            IdeaTreeVo ideaTree = new IdeaTreeVo();
            ideaTree.setId(id);
            ideaTree.setName(curIdea.getTitle());
            ideaTree.setChildren(new ArrayList<>());
            return Result.success(ideaTree);
        }*/
        while (rootId != -1) {
            LambdaQueryWrapper<Idea> curQueryWrapper = new LambdaQueryWrapper<>();
            curQueryWrapper.select(Idea::getId, Idea::getParentId, Idea::getTitle).eq(Idea::getId, rootId);
            curIdea = getOne(curQueryWrapper);
            rootId = curIdea.getParentId();
        }
        IdeaTreeVo ideaTree = getIdeaTreeWithChildren(curIdea);
        return Result.success(ideaTree);
    }

    @Override
    public Result search(String search) {
        if (Objects.isNull(search) || StringUtils.isBlank(search)) {
            return hotIdeas(5);
        }
        return Result.success(copyList(ideaMapper.searchIdeasByTitle(search), false, false, false, false));
    }

    @Override
    public Result getIdeasByAuthorId(Long authorId) {
        LambdaQueryWrapper<Idea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Idea::getAuthorId, authorId);
        queryWrapper.orderByDesc(Idea::getWeight, Idea::getCreateDate, Idea::getViewCounts);
        return Result.success(copyList(list(queryWrapper)));
    }

    @Override
    public Result listIdeaCollectionByUserId(Long id) {
        LambdaQueryWrapper<IdeaCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IdeaCollection::getUid, id);
        queryWrapper.select(IdeaCollection::getIid);
        List<IdeaCollection> collections = ideaCollectionService.list(queryWrapper);
        List<Idea> ideas = listByIds(collections.stream().map(IdeaCollection::getIid).collect(Collectors.toList()));
        return Result.success(copyList(ideas));
    }

    @Override
    public Result isCollected(Long uid, Long iid) {
        LambdaQueryWrapper<IdeaCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IdeaCollection::getUid, uid);
        queryWrapper.eq(IdeaCollection::getIid, iid);
        IdeaCollection collection = ideaCollectionService.getOne(queryWrapper);
        if (collection != null) {
            return Result.success(true);
        }
        return Result.success(false);
    }

    private IdeaTreeVo getIdeaTreeWithChildren(Idea root) {
        IdeaTreeVo ideaTree = new IdeaTreeVo();
        ideaTree.setId(root.getId());
        ideaTree.setName(root.getTitle());

        LambdaQueryWrapper<Idea> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Idea::getId, Idea::getTitle).eq(Idea::getParentId, root.getId());
        List<Idea> ideaChildren = list(queryWrapper);

        List<IdeaTreeVo> rootChildren = new ArrayList<>();
        if (Objects.nonNull(ideaChildren) && ideaChildren.size() > 0) {
            for (Idea ideaChild : ideaChildren) {
                rootChildren.add(getIdeaTreeWithChildren(ideaChild));
            }
        }
        ideaTree.setChildren(rootChildren);
        return ideaTree;
    }

    private List<IdeaVo> copyList(List<Idea> records) {
        return records.stream().map(this::copy).collect(Collectors.toList());
    }

    private List<IdeaVo> copyList(List<Idea> records, Boolean needTag, Boolean needAuthor, Boolean needBody, Boolean needCategory) {
        return records.stream().map(record -> copy(record, needTag, needAuthor, needBody, needCategory)).collect(Collectors.toList());
    }

    private IdeaVo copy(Idea idea) {
        return copy(idea, true, true, false, false);
    }

    private IdeaVo copy(Idea idea, Boolean needTag, Boolean needAuthor, Boolean needBody, Boolean needCategory) {
        IdeaVo ideaVo = new IdeaVo();
        BeanUtils.copyProperties(idea, ideaVo);
        ideaVo.setId(String.valueOf(idea.getId()));

        //查询收藏量
        LambdaQueryWrapper<IdeaCollection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IdeaCollection::getIid, idea.getId());
        int collectCounts = ideaCollectionService.count(queryWrapper);
        ideaVo.setCollectCounts(collectCounts);

        if (needTag) {
            Long ideaId = idea.getId();
            List<TagVo> tagVoList = tagService.findTagsByIdeaId(ideaId);
            ideaVo.setTags(tagVoList);
        }
        if (needAuthor) {
            Long authorId = idea.getAuthorId();
            SysUser sysUser = sysUserService.getById(authorId);
            UserVo authorVo = new UserVo();
            if (!Objects.isNull(sysUser)) {
                authorVo.setId(authorId);
                authorVo.setAvatar(sysUser.getAvatar());
                authorVo.setNickname(sysUser.getNickname());
            } else {
                authorVo.setAvatar("/static/user/admin.png");
                authorVo.setNickname("佚名");
            }
            ideaVo.setAuthor(authorVo);
        }
        if (needBody) {
            Long bodyId = idea.getBodyId();
            IdeaBody ideaBody = ideaBodyMapper.selectById(bodyId);
            IdeaBodyVo ideaBodyVo = new IdeaBodyVo();
            ideaBodyVo.setContent(ideaBody.getContent());
            ideaVo.setBody(ideaBodyVo);
        }
        if (needCategory) {
            Long categoryId = idea.getCategoryId();
            Category category = categoryService.getById(categoryId);
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);
            ideaVo.setCategory(categoryVo);
        }
        return ideaVo;
    }
}

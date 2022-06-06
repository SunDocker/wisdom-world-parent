package com.inspiration.vo;

import lombok.Data;

import java.util.List;

/**
 * @author SunDocker
 */
@Data
public class IdeaVo {

    /**
     * 防止Long精度损失
     */
    private String id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer collectCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private Long createDate;

    private UserVo author;

    private IdeaBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;

}
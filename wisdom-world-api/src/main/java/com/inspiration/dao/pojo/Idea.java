package com.inspiration.dao.pojo;

import lombok.Data;

/**
 * @author SunDocker
 */
@Data
public class Idea {

    public static final Integer IDEA_TOP = 1;

    public static final Integer IDEA_COMMON = 0;

    private Long id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    /**
     * 作者id
     */
    private Long authorId;
    /**
     * 内容id
     */
    private Long bodyId;
    /**
     *类别id
     */
    private Long categoryId;

    /**
     * 置顶
     */
    private Integer weight;

    /**
     * 创建时间
     */
    private Long createDate;

    private Long parentId;
}

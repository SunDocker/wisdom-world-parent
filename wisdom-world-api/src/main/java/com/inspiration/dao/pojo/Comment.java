package com.inspiration.dao.pojo;

import lombok.Data;

/**
 * @author SunDocker
 */
@Data
public class Comment {
    private Long id;

    private String content;

    private Long createDate;

    private Long ideaId;

    private Long authorId;

    private Long parentId;

    private Long toUid;

    private Integer level;
}

package com.inspiration.vo.params;

import lombok.Data;

/**
 * @author SunDocker
 */
@Data
public class CommentParam {
    private Long ideaId;
    private String content;
    private Long parentId;
    private Long toUserId;
}

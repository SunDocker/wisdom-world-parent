package com.inspiration.dao.pojo;

import lombok.Data;

/**
 * @author SunDocker
 */
@Data
public class IdeaBody {
    private Long id;
    private String content;
    private String contentHtml;
    private Long ideaId;
}

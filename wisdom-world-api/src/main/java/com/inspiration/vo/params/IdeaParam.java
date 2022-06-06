package com.inspiration.vo.params;

import com.inspiration.vo.CategoryVo;
import com.inspiration.vo.TagVo;
import lombok.Data;

import java.util.List;

/**
 * @author SunDocker
 */
@Data
public class IdeaParam {
    private Long id;
    private IdeaBodyParam body;
    private CategoryVo category;
    private String summary;
    private List<TagVo> tags;
    private String title;
    private Long parentId;
}

package com.inspiration.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author SunDocker
 */
@Data
public class CommentVo {
    /**
     * 防止精度损失
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String content;

    private Long createDate;

    private Integer level;

    private List<CommentVo> children;

    private UserVo toUser;

    private UserVo author;
}

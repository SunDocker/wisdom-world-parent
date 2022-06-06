package com.inspiration.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

/**
 * @author SunDocker
 */
@Data
public class IdeaTreeVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private List<IdeaTreeVo> children;
}

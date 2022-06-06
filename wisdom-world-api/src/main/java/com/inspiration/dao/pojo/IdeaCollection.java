package com.inspiration.dao.pojo;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (IdeaCollection)表实体类
 *
 * @author makejava
 * @since 2022-05-31 09:45:55
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ms_idea_collection")
public class IdeaCollection  {
    @TableId
    private Long id;

    private Long iid;
    
    private Long uid;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}

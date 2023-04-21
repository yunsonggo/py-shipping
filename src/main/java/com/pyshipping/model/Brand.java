package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 品牌模型
 */
@Data
public class Brand {
    private static final long brandVersionUID = 1L;

    private String id;
    private String name;
    private String categoryId;
    private String image;
    private String description;
    private Integer sort;
    private Integer isDeleted;
    // 自动填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    // 链表查询分类
    @TableField(exist = false)
    private Category category;

//    //一对多
//    @TableField(exist = false)
//    private List<Course> courses;
}

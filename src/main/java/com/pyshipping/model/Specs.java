package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 规格模型
 */
@Data
public class Specs {
    private static final long specsVersionUID = 1L;

    private String id;
    private String name;
    private String value;
    private Integer sort;

    // 自动填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;
}

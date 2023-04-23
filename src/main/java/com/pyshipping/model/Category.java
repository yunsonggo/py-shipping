package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 分类
 */
@Data
public class Category {
    private static final long serialVersionUID = -1;

    private String  id; //'主键'
    private Integer type;//'类型   1 商品分类 2 套餐分类'
    private String  name;//'分类名称'
    private String  icon;//图标
    private Integer sort;//'顺序',
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

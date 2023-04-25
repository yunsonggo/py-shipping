package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("套餐")
public class Setmeal {
    private static final long serialVersionUID = -1;

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("分类ID")
    private String categoryId;
    private String name;
    private BigDecimal price;
    private Integer status;
    private String code;
    private String description;
    private String image;
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

    @TableField(exist = false)
    private Integer page;
    @TableField(exist = false)
    private Integer size;
    @TableField(exist = false)
    private String minTime;
    @TableField(exist = false)
    private String maxTime;
}

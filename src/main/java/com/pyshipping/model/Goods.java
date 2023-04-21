package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Goods {
    private static final long goodsVersionUID = 1L;

    private String id;
    private String name;
    private String categoryId;
    private String brandId;
    private String specsId;
    private BigDecimal price;
    private BigDecimal marketPrice;
    private String code;
    private String image;
    private String description;
    private Integer status;
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

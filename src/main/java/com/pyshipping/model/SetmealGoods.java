package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SetmealGoods {
    private String id;
    private String brandId;
    private String brandName;
    private String setmealId;
    private String goodsId;
    private String name;
    private BigDecimal price; // 商品价格
    private Integer copies; // 数量
    private Integer sort;
    private Integer status;

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
}

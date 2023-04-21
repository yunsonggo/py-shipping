package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ShoppingCart {
    private static final long shoppingCartVersionUID = 1L;
    private String id ;
    private String name;
    private String image;
    private String userId;
    private String goodsId;
    private String setmealId;
    private String goodsSpecs;
    private Integer number; // 数量
    private BigDecimal amount; // 金额

    // 自动填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

package com.pyshipping.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.pyshipping.model.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GoodsDto extends Goods {

    @TableField(exist = false)
    Integer copies;
    @TableField(exist = false)
    Integer setmealSort;
    @TableField(exist = false)
    private Category category;
    @TableField(exist = false)
    private Brand brand;
    @TableField(exist = false)
    private GoodsSpecs goodsSpecs;
    @TableField(exist = false)
    private Specs specs;
}

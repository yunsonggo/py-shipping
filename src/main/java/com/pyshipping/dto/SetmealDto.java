package com.pyshipping.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.pyshipping.model.Category;
import com.pyshipping.model.Setmeal;
import com.pyshipping.model.SetmealGoods;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    @TableField(exist = false)
    private Category category;
    @TableField(exist = false)
    private GoodsDto[] goodsList;
    @TableField(exist = false)
    private List<SetmealGoods> setmealGoodsList;
}

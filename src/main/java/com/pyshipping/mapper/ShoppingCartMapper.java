package com.pyshipping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pyshipping.model.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}

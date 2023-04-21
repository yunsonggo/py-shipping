package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.mapper.SetmealGoodsMapper;
import com.pyshipping.model.SetmealGoods;
import com.pyshipping.service.SetmealGoodsSrv;
import org.springframework.stereotype.Service;

@Service
public class SetmealGoodsSrvImpl extends ServiceImpl<SetmealGoodsMapper, SetmealGoods> implements SetmealGoodsSrv {
}

package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.common.msg.CustomException;
import com.pyshipping.mapper.BrandMapper;
import com.pyshipping.model.Brand;
import com.pyshipping.model.Goods;
import com.pyshipping.model.Setmeal;
import com.pyshipping.service.BrandSrv;
import com.pyshipping.service.GoodsSrv;
import com.pyshipping.service.SetmealSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandSrvImpl extends ServiceImpl<BrandMapper, Brand> implements BrandSrv {

    @Autowired
    private GoodsSrv goodsSrv;
    @Autowired
    private SetmealSrv setmealSrv;

    @Override
    public void remove(String id) {
        LambdaQueryWrapper<Goods> goodsLqw = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> setmealLqw = new LambdaQueryWrapper<>();

        // 是否关联商品
        goodsLqw.eq(Goods::getCategoryId, id);
        int goodsCount = goodsSrv.count(goodsLqw);
        if (goodsCount > 0) {
            // 抛出业务异常
            throw new CustomException("当前品牌下有商品数据,不能删除");
        }
        // 是否关联套餐
        setmealLqw.eq(Setmeal::getCategoryId, id);
        int setmealCount = setmealSrv.count(setmealLqw);
        if (setmealCount > 0) {
            // 抛出业务异常
            throw new CustomException("当前品牌下有套餐数据,不能删除");
        }
        //正常删除
        super.removeById(id);
    }
}

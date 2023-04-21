package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.common.msg.CustomException;
import com.pyshipping.mapper.SpecsMapper;
import com.pyshipping.model.GoodsSpecs;
import com.pyshipping.model.Specs;
import com.pyshipping.service.GoodsSpecsSrv;
import com.pyshipping.service.SpecsSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecsSrvImpl extends ServiceImpl<SpecsMapper, Specs> implements SpecsSrv {
    @Autowired
    private GoodsSpecsSrv goodsSpecsSrv;

    @Override
    public void remove(String id) {
        // 是否有商品关联
        LambdaQueryWrapper<GoodsSpecs> gsLqw = new LambdaQueryWrapper<>();
        gsLqw.eq(GoodsSpecs::getSpecsId,id);
        int count = goodsSpecsSrv.count(gsLqw);
        if (count > 0) {
            throw new CustomException("有商品关联规格,不能删除");
        }
        // 正常删除
        super.removeById(id);
    }
}

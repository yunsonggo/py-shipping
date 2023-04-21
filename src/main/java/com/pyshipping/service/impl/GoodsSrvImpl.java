package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.common.msg.CustomException;
import com.pyshipping.dto.GoodsDto;
import com.pyshipping.mapper.GoodsMapper;
import com.pyshipping.model.Goods;
import com.pyshipping.service.GoodsSrv;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsSrvImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsSrv {
    public void saveWithSpecs(GoodsDto goodsdto) {
        // 保存商品基本信息
        this.save(goodsdto);
    }

    /**
     * 批量修改状态
     * @param ids
     * @param status
     */
    @Override
    @Transactional
    public void batchEditStatus(List<String> ids, Integer status) {
        LambdaQueryWrapper<Goods> lqw = new LambdaQueryWrapper<>();
        lqw.select(Goods::getId,Goods::getStatus);
        lqw.in(Goods::getId,ids);
        List<Goods> list = this.list(lqw);

        list.forEach((item) -> {
            item.setStatus(status);
        });
        this.updateBatchById(list);
    }

    /**
     * 批量删除商品
     * @param ids
     */
    @Override
    public void batchDelete(List<String> ids) {
        LambdaQueryWrapper<Goods> lqw = new LambdaQueryWrapper<>();
        lqw.in(Goods::getId,ids);
        lqw.eq(Goods::getStatus,1);
        int Count = this.count(lqw);
        if (Count > 0) {
            throw new CustomException("商品正在售卖中,不能删除");
        }
        this.removeByIds(ids);
    }
}

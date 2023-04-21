package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.dto.GoodsDto;
import com.pyshipping.dto.SetmealDto;
import com.pyshipping.mapper.SetmealMapper;
import com.pyshipping.model.Brand;
import com.pyshipping.model.Category;
import com.pyshipping.model.Setmeal;
import com.pyshipping.model.SetmealGoods;
import com.pyshipping.service.BrandSrv;
import com.pyshipping.service.CategorySrv;
import com.pyshipping.service.SetmealGoodsSrv;
import com.pyshipping.service.SetmealSrv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealSrvImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealSrv {
    @Autowired
    private SetmealGoodsSrv sgSrv;
    // 保存套餐及套餐商品
    // 开启spring事务
    @Override
    @Transactional
    public void saveWithGoods(SetmealDto setmealDto) {
        // 保存套餐基本信息
        this.save(setmealDto);
        // 保存套餐和商品关联数据
        List<GoodsDto> goodsDtoList = List.of(setmealDto.getGoodsList());
        List<SetmealGoods> list = goodsDtoList.stream().map((item) ->{
            SetmealGoods sg = new SetmealGoods();
            sg.setBrandId(item.getBrandId());
            sg.setBrandName(item.getBrand().getName());
            sg.setSetmealId(setmealDto.getId());
            sg.setGoodsId(item.getId());
            sg.setName(item.getName());
            sg.setPrice(item.getPrice());
            sg.setCopies(item.getCopies());
            sg.setSort(item.getSetmealSort());
            sg.setStatus(item.getStatus());
            return sg;
        }).collect(Collectors.toList());
        sgSrv.saveBatch(list);
    }

    /**
     * 修改套餐及套餐商品
     * @param setmealDto
     */
    @Override
    @Transactional
    public void editWithGoods(SetmealDto setmealDto) {
        // 修改基本信息
        this.updateById(setmealDto);

        // 商品关联数据
        List<GoodsDto> goodsDtoList = List.of(setmealDto.getGoodsList());

        // 删除套餐商品
        LambdaQueryWrapper<SetmealGoods> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SetmealGoods::getSetmealId,setmealDto.getId());
        sgSrv.remove(lqw);

        // 保存套餐和商品关联数据
        List<SetmealGoods> list = goodsDtoList.stream().map((item) ->{
            SetmealGoods sg = new SetmealGoods();
            sg.setBrandId(item.getBrandId());
            sg.setBrandName(item.getBrand().getName());
            sg.setSetmealId(setmealDto.getId());
            sg.setGoodsId(item.getId());
            sg.setName(item.getName());
            sg.setPrice(item.getPrice());
            sg.setCopies(item.getCopies());
            sg.setSort(item.getSetmealSort());
            return sg;
        }).collect(Collectors.toList());
        sgSrv.saveBatch(list);
    }

    /**
     * 删除套餐
     * @param id
     */
    @Override
    @Transactional
    public void removeSetmeal(String id) {
        LambdaQueryWrapper<SetmealGoods> sgLqw = new LambdaQueryWrapper<>();
        sgLqw.eq(SetmealGoods::getSetmealId,id);
        sgSrv.remove(sgLqw);
        this.removeById(id);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void batchRemoveSetmeal(List<String> ids) {
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealGoods> sgLqw = new LambdaQueryWrapper<>();
        sgLqw.in(SetmealGoods::getSetmealId,ids);
        sgSrv.remove(sgLqw);
    }

    @Override
    @Transactional
    public void batchEditStatus(List<String> ids, Integer status) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.select(Setmeal::getId,Setmeal::getStatus);
        lqw.in(Setmeal::getId,ids);
        List<Setmeal> list = this.list(lqw);
        list.forEach((item) -> {
            item.setStatus(status);
        });
        this.updateBatchById(list);
    }
}

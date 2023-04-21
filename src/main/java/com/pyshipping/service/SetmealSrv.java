package com.pyshipping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pyshipping.dto.SetmealDto;
import com.pyshipping.model.Setmeal;

import java.util.List;

public interface SetmealSrv extends IService<Setmeal> {
    // 添加保存套餐及套餐商品
    public void saveWithGoods(SetmealDto setmealDto);
    // 修改套餐及套餐商品
    public void editWithGoods(SetmealDto setmealDto);
    // 删除套餐
    public void removeSetmeal(String id);
    //批量删除套餐
    public void batchRemoveSetmeal(List<String> ids);
    // 批量修改套餐状态
    public void batchEditStatus(List<String> ids,Integer status);
}

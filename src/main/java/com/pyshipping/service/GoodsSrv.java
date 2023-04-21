package com.pyshipping.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pyshipping.dto.GoodsDto;
import com.pyshipping.model.Goods;

import java.util.List;

public interface GoodsSrv extends IService<Goods> {
    public void saveWithSpecs(GoodsDto goodsdto);

    // 批量修改状态
    public void batchEditStatus(List<String> ids,Integer status);

    // 批量删除商品
    void batchDelete(List<String> ids);
}

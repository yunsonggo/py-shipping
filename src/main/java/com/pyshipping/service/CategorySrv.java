package com.pyshipping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pyshipping.model.Category;

public interface CategorySrv extends IService<Category> {
    // 删除分类服务方法
    public void remove(String id);
}

package com.pyshipping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pyshipping.model.Specs;

public interface SpecsSrv extends IService<Specs> {

    // 删除规格
    public void remove(String id);
}

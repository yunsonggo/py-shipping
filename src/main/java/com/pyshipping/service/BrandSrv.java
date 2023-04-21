package com.pyshipping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pyshipping.model.Brand;

public interface BrandSrv extends IService<Brand> {
    public void remove(String id);
}

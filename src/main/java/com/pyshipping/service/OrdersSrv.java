package com.pyshipping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pyshipping.model.Orders;

public interface OrdersSrv extends IService<Orders> {

    // 下单
    public void add(Orders orders);
}

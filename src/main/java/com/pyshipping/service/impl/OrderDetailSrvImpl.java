package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.mapper.OrderDetailMapper;
import com.pyshipping.model.OrderDetail;
import com.pyshipping.service.OrderDetailSrv;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailSrvImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailSrv {
}

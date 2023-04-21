package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.mapper.ShoppingCartMapper;
import com.pyshipping.model.ShoppingCart;
import com.pyshipping.service.ShoppingCartSrv;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartSrvImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartSrv {
}

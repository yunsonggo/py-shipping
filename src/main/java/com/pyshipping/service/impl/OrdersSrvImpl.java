package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.common.autosave.BaseContext;
import com.pyshipping.common.msg.CustomException;
import com.pyshipping.mapper.OrdersMapper;
import com.pyshipping.model.*;
import com.pyshipping.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrdersSrvImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersSrv {
    @Autowired
    private ShoppingCartSrv shoppingCartSrv;
    @Autowired
    private UserSrv userSrv;
    @Autowired
    private AddressBookSrv addressBookSrv;

    @Autowired
    private OrderDetailSrv orderDetailSrv;


    @Override
    public void add(Orders orders) {
        String userId = BaseContext.getUserId();
        // 查询购物车数据
        LambdaQueryWrapper<ShoppingCart> shoppingCartLqw = new LambdaQueryWrapper<>();
        shoppingCartLqw.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartSrv.list(shoppingCartLqw);
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new CustomException("购物车是空的");
        }

        // 查询用户数据
        User user = userSrv.getById(userId);

        // 查询地址数据
        AddressBook addressBook = addressBookSrv.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new CustomException("收货地址不能为空");
        }

        // 生成订单号
        String sn = IdWorker.getIdStr();
        // 总金额
        AtomicInteger amount = new AtomicInteger(0);

        // 订单详情数据(购物车中多个商品或套餐的数据)
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map((item) -> {
            OrderDetail detail = new OrderDetail();
            detail.setOrderSn(sn);
            detail.setNumber(item.getNumber());
            detail.setName(item.getName());
            detail.setImage(item.getImage());
            detail.setAmount(item.getAmount());
            if (item.getGoodsId() != null) {
                detail.setGoodsId(item.getGoodsId());
                detail.setGoodsSpecs(item.getGoodsSpecs());
            } else if (item.getSetmealId() != null) {
                detail.setSetmealId(item.getSetmealId());
            }
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return detail;
        }).collect(Collectors.toList());

        // 订单数据
        orders.setSn(sn);
        orders.setStatus(1);
        orders.setUserId(userId);
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail())
        );
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserName(user.getUsername());
        // 保存订单
        this.save(orders);
        // 保存订单详情
        orderDetailSrv.saveBatch(orderDetailList);

        // 情况购物车
        shoppingCartSrv.remove(shoppingCartLqw);
    }
}

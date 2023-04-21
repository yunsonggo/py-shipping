package com.pyshipping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pyshipping.common.autosave.BaseContext;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.model.ShoppingCart;
import com.pyshipping.service.GoodsSrv;
import com.pyshipping.service.SetmealSrv;
import com.pyshipping.service.ShoppingCartSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartSrv srv;

    // 添加购物车 或 覆盖购物车商品/套餐数量
    @PostMapping
    public Msg<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        String userId = BaseContext.getUserId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,userId);

        // 商品还是套餐
        if (shoppingCart.getGoodsId() != null) {
            lqw.eq(ShoppingCart::getGoodsId,shoppingCart.getGoodsId());
        } else {
            lqw.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        // 查询当前商品或套餐是否已存在购物车
        ShoppingCart savedCart = srv.getOne(lqw);
        // 如果已存在
        if (savedCart != null) {
            savedCart.setNumber(shoppingCart.getNumber());
            srv.updateById(savedCart);
        } else {
            // 如果不存在
            srv.save(shoppingCart);
            savedCart = shoppingCart;
        }
        return Msg.success(Codes.OK,"ok",savedCart);
    }

    // 购物车列表
    @GetMapping("/list")
    public Msg<List<ShoppingCart>> list() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getUserId());
        lqw.orderByDesc(ShoppingCart::getUpdateTime);
        List<ShoppingCart> list = srv.list(lqw);
        return Msg.success(Codes.OK,"ok",list);
    }

    // 清空购物车
    @DeleteMapping("/clean")
    public Msg<String> clean() {
        LambdaQueryWrapper<ShoppingCart> lqw = new LambdaQueryWrapper<>();
        lqw.eq(ShoppingCart::getUserId,BaseContext.getUserId());
        srv.remove(lqw);
        return Msg.success(Codes.OK,"已清空","ok");
    }

    // 删除购物车商品/套餐
    @DeleteMapping("{id}")
    public Msg<String> remove(@PathVariable("id") String id) {
        srv.removeById(id);
        return Msg.success(Codes.OK,"删除成功","ok");
    }
}

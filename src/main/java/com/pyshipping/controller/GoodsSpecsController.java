package com.pyshipping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.model.GoodsSpecs;
import com.pyshipping.service.GoodsSpecsSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品规格
 */
@Slf4j
@RestController
@RequestMapping("/goods/specs")
public class GoodsSpecsController {
    @Autowired
    private GoodsSpecsSrv srv;

    @GetMapping
    public Msg<Page> list(Integer page, Integer size) {
        // 分页构造器
        Page<GoodsSpecs> limited = new Page<>(page,size);
        LambdaQueryWrapper<GoodsSpecs> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(GoodsSpecs::getUpdateTime);
        srv.page(limited, lqw);
        return Msg.success(Codes.OK, "ok", limited);
    }



}

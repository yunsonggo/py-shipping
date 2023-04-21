package com.pyshipping.controller;

import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.model.Orders;
import com.pyshipping.service.OrdersSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersSrv srv;

    // 下单
    @PostMapping
    public Msg<String> add(@RequestBody Orders orders) {
        srv.add(orders);
        return Msg.success(Codes.OK,"下单成功","ok");
    }

}

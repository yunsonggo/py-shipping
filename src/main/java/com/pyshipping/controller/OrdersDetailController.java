package com.pyshipping.controller;

import com.pyshipping.service.OrderDetailSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order_detail")
public class OrdersDetailController {
    @Autowired
    private OrderDetailSrv srv;

}

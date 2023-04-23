package com.pyshipping.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetail {
    private static final long serialVersionUID = -1;

    private String id;
    private String name;
    private String image;
    private String orderSn;
    private String goodsId;
    private String setmealId;
    private String goodsSpecs;
    private Integer number;
    private BigDecimal amount;
}

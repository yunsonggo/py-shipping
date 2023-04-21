package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Orders {
    private static final long ordersVersionUID = 1L;

    private String id;
    private String sn; // 订单号
    private Integer status; //订单状态 1待付款,2待派送,3已派送,4已完成,5已取消
    private String userId;
    private String addressBookId; // 地址id
    private Integer payMethod; // 支付方式 1 微信 2 支付宝
    private BigDecimal amount; // 实收金额
    private String remark;
    private String phone;
    private String address; // 地址
    private String userName; // 用户名
    private String consignee; // 收货人
    private LocalDateTime orderTime; // 下单时间
    private LocalDateTime checkoutTime; // 结账时间

    // 自动填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

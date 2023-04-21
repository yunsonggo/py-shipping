package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressBook {
    private static final long addressBookVersionUID = 1L;

    private String id;
    private String userId;
    private String consignee;// 收货人
    private Integer sex;
    private String phone;
    private String provinceCode; // 省编号
    private String provinceName; // 省名
    private String cityCode; // 市编号
    private String cityName; // 市名
    private String districtCode; // 区编号
    private String districtName; // 区
    private String detail; // 地址
    private String label;  // 标签
    private Integer isDefault; // 0 非 1是默认
    private Integer isDeleted;
    // 自动填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;
}

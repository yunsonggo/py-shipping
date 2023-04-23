package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private static final long serialVersionUID = -1;

    private String id;
    private String name;
    private String username;
    private String password;
    private String phone;
    private String sex;

    private String idNumber;
    private Integer status;

    private String avatar;

    private LocalDateTime birthday;

    // 自动填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    // 不参与数据库交互字段
    @TableField(exist = false)
    private String captchaId;
    @TableField(exist = false)
    private String captchaCode;
    @TableField(exist = false)
    private String token;

    public boolean verifyFields() {
        if (this.name.equals("") | this.phone.equals("")) {
            return false;
        } else if (this.sex.equals("")) {
            this.sex = "男";
        } else if (this.username.equals("")) {
            this.username = "游客";
        } else {
            this.status = 1;
        }
        return true;
    }
}

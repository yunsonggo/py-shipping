package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 员工
 */
@Data
public class Employee {
    private static final long serialVersionUID = -1;

    private String id;
    private String name;
    private String username;
    private String password;
    private String phone;
    private String sex;
    private String idNumber;
    private Integer status;

    // 自动填充字段
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private String createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    // 不参与数据库交互字段
    @TableField(exist = false)
    private String captchaId;
    @TableField(exist = false)
    private String captchaCode;
    @TableField(exist = false)
    private String token;

    public boolean verifyFields() {
        if (this.username.equals("") | this.password.equals("") | this.name.equals("") | this.phone.equals("") | this.idNumber.equals("")) {
            return false;
        } else if (this.sex.equals("")) {
            this.sex = "男";
        } else {
            this.status = 1;
        }
        return true;
    }
}

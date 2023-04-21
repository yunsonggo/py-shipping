package com.pyshipping.model;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class Uploads {
    @TableField(exist = false)
    private String type;
    @TableField(exist = false)
    private String oldPath;
}

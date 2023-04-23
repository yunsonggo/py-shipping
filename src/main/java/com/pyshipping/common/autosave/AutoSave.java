package com.pyshipping.common.autosave;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 公共字段 自动填充
 */
@Component
@Slf4j
public class AutoSave implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());

        // 通过线程ThreadLocal获取当前已登录用户的ID
        String id = BaseContext.getEmployeeId();
        metaObject.setValue("createUser",id);
        metaObject.setValue("updateUser",id);
        log.info("auto insert fill");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime",LocalDateTime.now());
        // 通过线程ThreadLocal获取当前已登录用户的ID
        String id = BaseContext.getEmployeeId();
        metaObject.setValue("updateUser",id);
        log.info("auto update fill");
    }
}

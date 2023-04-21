package com.pyshipping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pyshipping.common.autosave.BaseContext;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.model.AddressBook;
import com.pyshipping.service.AddressBookSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/address")
public class AddressBookController {
    @Autowired
    private AddressBookSrv srv;

    /**
     * 添加地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public Msg<AddressBook> add(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getUserId());
        srv.save(addressBook);
        return Msg.success(Codes.OK,"添加成功",addressBook);
    }

    /**
     * 设置为默认地址
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public Msg<String> setDefault(@RequestBody AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> lqw = new LambdaUpdateWrapper<>();
        lqw.eq(AddressBook::getUserId,BaseContext.getUserId());
        // 设置当前用户ID下的所有地址都是非默认
        lqw.set(AddressBook::getIsDefault,0);
        srv.update(lqw);
        // 设置当前地址为默认
        addressBook.setIsDefault(1);
        srv.updateById(addressBook);
        return Msg.success(Codes.OK,"设置成功","ok");
    }

    /**
     * id 查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Msg<AddressBook> getById(@PathVariable String id) {
        AddressBook addressBook = srv.getById(id);
        if (addressBook != null) {
            return Msg.success(Codes.OK,"ok",addressBook);
        } else {
            return Msg.error(Codes.NotFound,"没有找到","param error");
        }
    }

    /**
     * 查询默认地址
     * @return
     */
    @GetMapping("/default")
    public Msg<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(AddressBook::getUserId,BaseContext.getUserId());
        lqw.eq(AddressBook::getIsDefault,1);
        AddressBook addressBook = srv.getOne(lqw);
        if (addressBook == null) {
            return Msg.error(Codes.NotFound,"没有默认地址","not found");
        } else {
            return Msg.success(Codes.OK,"ok",addressBook);
        }
    }

    /**
     * 获取用户地址列表
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public Msg<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getUserId());
        LambdaQueryWrapper<AddressBook> lqw = new LambdaQueryWrapper<>();
        lqw.eq(addressBook.getUserId() != null,AddressBook::getUserId,addressBook.getUserId());
        lqw.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = srv.list(lqw);
        return Msg.success(Codes.OK,"ok",list);
    }
}

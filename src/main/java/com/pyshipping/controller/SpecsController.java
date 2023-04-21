package com.pyshipping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.model.Specs;
import com.pyshipping.service.SpecsSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/specs")
public class SpecsController {
    @Autowired
    private SpecsSrv srv;

    /**
     * 添加规格
     * @param specs
     * @return
     */
    @PostMapping
    public Msg<Specs> add(@RequestBody  Specs specs) {
        srv.save(specs);
        return Msg.success(Codes.OK,"ok",specs);
    }

    /**
     * 规格分页数据
     * @param page
     * @param size
     * @return
     */
    @GetMapping
    public Msg<Page> list(Integer page,Integer size) {
        Page<Specs> limited = new Page<>(page,size);
        LambdaQueryWrapper<Specs> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Specs::getSort);
        srv.page(limited,lqw);
        return Msg.success(Codes.OK,"添加成功!",limited);
    }

    /**
     * 修改规格
     * @param specs
     * @return
     */
    @PutMapping
    public Msg<Specs> edit(@RequestBody Specs specs) {
        srv.updateById(specs);
        return  Msg.success(Codes.OK,"ok",specs);
    }

    /**
     * 删除规格
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public Msg<String> delete(@PathVariable("id") String id){
        srv.remove(id);
        return Msg.success(Codes.OK,"删除成功!",id);
    }

    /**
     * 获取 规格 名称列表
     * @return
     */
    @GetMapping("/names")
    public Msg<List<Specs>> specsNames() {
        QueryWrapper<Specs> qw = new QueryWrapper<>();
        qw.select("DISTINCT name");
        List<Specs> specsList = srv.list(qw);
        return Msg.success(Codes.OK,"ok",specsList);
    }

    @GetMapping("/name")
    public Msg<List<Specs>> listByName(String name){
        LambdaQueryWrapper<Specs> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Specs::getName,name);
        lqw.orderByAsc(Specs::getSort);
        List<Specs> list = srv.list(lqw);
        return Msg.success(Codes.OK,"ok",list);
    }
}

package com.pyshipping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.dto.SetmealDto;
import com.pyshipping.model.Category;
import com.pyshipping.model.Setmeal;
import com.pyshipping.model.SetmealGoods;
import com.pyshipping.service.BrandSrv;
import com.pyshipping.service.CategorySrv;
import com.pyshipping.service.SetmealGoodsSrv;
import com.pyshipping.service.SetmealSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    public static final String basePath = "src/main/resources/static/";
    @Autowired
    private SetmealSrv srv;
    @Autowired
    private CategorySrv categorySrv;
    @Autowired
    private SetmealGoodsSrv setmealGoodsSrv;

    @Autowired
    private BrandSrv brandSrv;

    // 条件分页数据
    @GetMapping("/list")
    public Msg<Page> list(Setmeal setmeal) {
        // 分页构造器
        Integer page = setmeal.getPage();
        Integer size = setmeal.getSize();
        if (page == null) {
            page = 1;
        }
        if (size == null) {
            size = 5;
        }
        LocalDateTime minTime = null;
        LocalDateTime maxtime = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (!Objects.equals(setmeal.getMinTime(), "")) {
            minTime = LocalDateTime.parse(setmeal.getMinTime(),df);
        }
        if (!Objects.equals(setmeal.getMaxTime(), "")) {
            maxtime = LocalDateTime.parse(setmeal.getMaxTime(),df);
        }
        Page<Setmeal> limited = new Page<>(page,size);
        // 条件
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.like(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        lqw.like(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        lqw.ge(minTime !=null, Setmeal::getUpdateTime,minTime);
        lqw.le(maxtime != null,Setmeal::getUpdateTime,maxtime);
        lqw.like(setmeal.getName() != null,Setmeal::getName,setmeal.getName());
        lqw.like(setmeal.getCode() != null,Setmeal::getCode,setmeal.getCode());
        lqw.orderByDesc(Setmeal::getSort);
        srv.page(limited,lqw);
        //DTO 扩展 分类
        Page<SetmealDto> dtoPage = new Page<>();
        BeanUtils.copyProperties(limited,dtoPage,"records");
        List<Setmeal> records = limited.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto sDto = new SetmealDto();
            BeanUtils.copyProperties(item,sDto);
            String cId = item.getCategoryId();
            Category c = categorySrv.getById(cId);
            sDto.setCategory(c);
            LambdaQueryWrapper<SetmealGoods> sgLqw = new LambdaQueryWrapper<>();
            sgLqw.eq(SetmealGoods::getSetmealId,item.getId());
            List<SetmealGoods> sgList = setmealGoodsSrv.list(sgLqw);
            sDto.setSetmealGoodsList(sgList);
            return sDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return Msg.success(Codes.OK,"ok",dtoPage);
    }

    /**
     * 添加套餐及套餐商品
     * @param setmealDto
     * @return
     */
    @PostMapping
    public Msg<String> add(@RequestBody SetmealDto setmealDto) {
        srv.saveWithGoods(setmealDto);
        return Msg.success(Codes.OK,"添加成功","ok");
    }

    @PutMapping
    public Msg<String> edit(@RequestBody SetmealDto setmealDto) {
        srv.editWithGoods(setmealDto);
        return Msg.success(Codes.OK,"修改成功","ok");
    }

    /**
     * id删除套餐
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public Msg<String> remove(@PathVariable("id") String id) {
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.select(Setmeal::getStatus,Setmeal::getImage);
        lqw.eq(Setmeal::getId,id);
        //Setmeal sg = srv.getById(id);
        Setmeal sg = srv.getOne(lqw);
        Integer status = sg.getStatus();
        if (status.equals(1)) {
            return Msg.error(Codes.FailedPrecondition,"只能删除已停售的套餐","");
        } else if (status.equals(0)) {
            srv.removeSetmeal(id);
            if (sg.getImage() != null) {
                // 删除文件
                String oldFilePath = basePath + sg.getImage();
                File old = new File(oldFilePath);
                if (old.isFile() && old.exists()) {
                    old.delete();
                }
            }
            return Msg.success(Codes.OK,"删除成功","ok");
        } else {
            return Msg.error(Codes.Internal,"套餐状态错误","请检查套餐状态");
        }
    }
    // 批量删除
    @DeleteMapping
    public Msg<String> batchRemove(@RequestParam List<String> ids) {
        log.info("ids:{}",ids);
        //List<Setmeal> list = srv.listByIds(ids);
        // 如果有售卖中的商品 取消删除
        LambdaQueryWrapper<Setmeal> isSaleLqw = new LambdaQueryWrapper<>();
        isSaleLqw.in(Setmeal::getId,ids);
        isSaleLqw.eq(Setmeal::getStatus,1);
        int count = srv.count(isSaleLqw);
        if (count > 0) {
            return Msg.error(Codes.FailedPrecondition,"售卖中的套餐不能删除","请检查套餐状态");
        }
        // 查询删除数据 准备删除图片
        LambdaQueryWrapper<Setmeal> lqw = new LambdaQueryWrapper<>();
        lqw.select(Setmeal::getId,Setmeal::getStatus,Setmeal::getImage);
        lqw.in(Setmeal::getId,ids);
        List<Setmeal> list = srv.list(lqw);
        // 删除套餐及套餐商品关联数据
        srv.batchRemoveSetmeal(ids);
        // 删除图片
        list.forEach(item -> {
            if (item.getImage() != null) {
                // 删除文件
                String oldFilePath = basePath + item.getImage();
                File old = new File(oldFilePath);
                if (old.isFile() && old.exists()) {
                    if (old.delete()){
                        log.info("删除套餐图片:{}",oldFilePath);
                    }
                }
            }
        });
        return Msg.success(Codes.OK,"批量删除成功","ok");
    }

    @GetMapping("/status")
    public Msg<String> batchEditStatus(@RequestParam List<String> ids,@RequestParam Integer status) {
        srv.batchEditStatus(ids,status);
        return Msg.success(Codes.OK,"批量修改成功","ok");
    }
}


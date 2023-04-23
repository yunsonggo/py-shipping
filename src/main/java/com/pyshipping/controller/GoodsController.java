package com.pyshipping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.keys.Keys;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.dto.GoodsDto;
import com.pyshipping.model.*;
import com.pyshipping.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 商品
 */
@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsSrv srv;

    @Autowired
    private SpecsSrv specsSrv;

    @Autowired
    private GoodsSpecsSrv goodsSpecsSrv;

    @Autowired
    private CategorySrv categorySrv;
    @Autowired
    private BrandSrv brandSrv;

    @Autowired
    private Keys keys;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


    // 可带条件查询分页数据
    @GetMapping("/list")
    public Msg<Page> list(Goods goods) {
        // 分页构造器
        Integer page = goods.getPage();
        Integer size = goods.getSize();

        LocalDateTime minTime = null;
        LocalDateTime maxtime = null;
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (!Objects.equals(goods.getMinTime(), "")) {
            minTime = LocalDateTime.parse(goods.getMinTime(),df);
        }
        if (!Objects.equals(goods.getMaxTime(), "")) {
            maxtime = LocalDateTime.parse(goods.getMaxTime(),df);
        }


        Page<Goods> limited = new Page<Goods>(page,size);
        // 条件构造器
        LambdaQueryWrapper<Goods> lqw = new LambdaQueryWrapper<>();
        lqw.like(goods.getCategoryId()!= null,Goods::getCategoryId,goods.getCategoryId());
        lqw.like(goods.getBrandId() != null,Goods::getBrandId,goods.getBrandId());
        lqw.like(goods.getStatus() != null,Goods::getStatus,goods.getStatus());
        lqw.ge(minTime !=null,Goods::getUpdateTime,minTime);
        lqw.le(maxtime != null,Goods::getUpdateTime,maxtime);
        lqw.like(goods.getName() != null,Goods::getName,goods.getName());
        lqw.like(goods.getCode() != null,Goods::getCode,goods.getCode());
        // 排序
        lqw.orderByDesc(Goods::getUpdateTime);
        srv.page(limited,lqw);

        // DTO 扩展对象 整合 分类 品牌数据
        Page<GoodsDto> dtoPage = new Page<>();
        BeanUtils.copyProperties(limited,dtoPage,"records");
        // 处理records 添加分类品牌信息
        List<Goods> records = limited.getRecords();

        List<GoodsDto> list = records.stream().map((item) -> {
            GoodsDto gDto = new GoodsDto();
            // 拷贝item 到 gDto
            BeanUtils.copyProperties(item,gDto);

            String cId = item.getCategoryId();
            String bId = item.getBrandId();
            String sId = item.getSpecsId();
            Category c = categorySrv.getById(cId);
            Brand b = brandSrv.getById(bId);
            Specs s = specsSrv.getById(sId);

            // 赋值 category brand
            gDto.setCategory(c);
            gDto.setBrand(b);
            gDto.setSpecs(s);

            return gDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return Msg.success(Codes.OK,"ok",dtoPage);
    }

    // 用户端缓存 分类商品 分页数据 只按分类查询的数据
    @GetMapping("/page")
    public Msg<Page> pageList(Goods goods) {
        List<GoodsDto> list = null;
        Page<GoodsDto> dtoPage = new Page<>();
        // 分页构造器
        Integer page = goods.getPage();
        Integer size = goods.getSize();
        // 构建 缓存 key
        String categoryId = null;
        if (goods.getCategoryId() == null) {
            LambdaQueryWrapper<Category> categoryLqw = new LambdaQueryWrapper<>();
            categoryLqw.eq(Category::getType,1);
            categoryLqw.orderByAsc(Category::getSort);
            List<Category> categoryRes = categorySrv.list(categoryLqw);
            categoryId = categoryRes.get(0).getId();
        } else {
            categoryId = goods.getCategoryId();
        }
        String redisKey = keys.getCategoryGoodsRedisKey(categoryId,1,page,size);
        String totalKey = keys.getCategoryGoodsTotalRedisKey(categoryId);
        // 先从缓存获取数据
        list = (List<GoodsDto>) redisTemplate.opsForValue().get(redisKey);
        if (list != null) {
            dtoPage.setRecords(list);
            Integer total = (Integer) redisTemplate.opsForValue().get(totalKey);
            if (total != null) {
                dtoPage.setTotal(total);
            }
            return Msg.success(Codes.OK,"ok",dtoPage);
        }

        // 没有缓存 查询数据库
        Page<Goods> limited = new Page<Goods>(page,size);
        // 条件构造器
        LambdaQueryWrapper<Goods> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Goods::getCategoryId,categoryId);
        lqw.eq(Goods::getStatus,1);
        // 排序
        lqw.orderByAsc(Goods::getSort);
        lqw.orderByDesc(Goods::getUpdateTime);
        srv.page(limited,lqw);

        // DTO 扩展对象 整合 分类 品牌数据

        BeanUtils.copyProperties(limited,dtoPage,"records");
        // 处理records 添加分类品牌信息
        List<Goods> records = limited.getRecords();

        list = records.stream().map((item) -> {
            GoodsDto gDto = new GoodsDto();
            // 拷贝item 到 gDto
            BeanUtils.copyProperties(item,gDto);

            String cId = item.getCategoryId();
            String bId = item.getBrandId();
            String sId = item.getSpecsId();
            Category c = categorySrv.getById(cId);
            Brand b = brandSrv.getById(bId);
            Specs s = specsSrv.getById(sId);

            // 赋值 category brand
            gDto.setCategory(c);
            gDto.setBrand(b);
            gDto.setSpecs(s);

            return gDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);
        // 设置 缓存数据
        redisTemplate.opsForValue().set(redisKey,list,60, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(totalKey,limited.getTotal(),60,TimeUnit.MINUTES);

        return Msg.success(Codes.OK,"ok",dtoPage);
    }


    // 添加商品
    @PostMapping
    public Msg<GoodsDto> add(@RequestBody  GoodsDto goodsdto) {
        srv.saveWithSpecs(goodsdto);

        // 删除所有缓存数据
        String redisPrefixKey = keys.getCategoryGoodsRedisKeyPrefix();
        Set<Object> goodsKeys = redisTemplate.keys(redisPrefixKey);
        if (goodsKeys != null) {
            redisTemplate.delete(goodsKeys);
        }

        return Msg.success(Codes.OK,"ok",goodsdto);
    }

    // 修改商品
    @PutMapping
    public Msg<String> edit(@RequestBody  Goods goods) {
        srv.updateById(goods);

        // 删除所有缓存数据
        String redisPrefixKey = keys.getCategoryGoodsRedisKeyPrefix();
        Set<Object> goodsKeys = redisTemplate.keys(redisPrefixKey);
        if (goodsKeys != null) {
            redisTemplate.delete(goodsKeys);
        }

        return Msg.success(Codes.OK,"ok","ok");
    }
    // 批量修改状态
    @GetMapping("/status")
    public Msg<String> batchEditStatus(@RequestParam List<String> ids, Integer status) {
        srv.batchEditStatus(ids,status);

        // 删除所有缓存数据
        String redisPrefixKey = keys.getCategoryGoodsRedisKeyPrefix();
        Set<Object> goodsKeys = redisTemplate.keys(redisPrefixKey);
        if (goodsKeys != null) {
            redisTemplate.delete(goodsKeys);
        }

        return Msg.success(Codes.OK,"批量修改成功","ok");
    }
    // 删除商品
    @DeleteMapping
    public Msg<String> batchDelete(@RequestParam List<String> ids) {
        srv.batchDelete(ids);

        // 删除所有缓存数据
        String redisPrefixKey = keys.getCategoryGoodsRedisKeyPrefix();
        Set<Object> goodsKeys = redisTemplate.keys(redisPrefixKey);
        if (goodsKeys != null) {
            redisTemplate.delete(goodsKeys);
        }

        return Msg.success(Codes.OK,"批量删除成功","ok");
    }

}

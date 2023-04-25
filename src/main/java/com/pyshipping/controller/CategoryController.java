package com.pyshipping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.model.Category;
import com.pyshipping.service.CategorySrv;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@Api(tags = "分类接口")
public class CategoryController {

    @Autowired
    private CategorySrv srv;

    /**
     * 分类列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("")
    @ApiOperation(value = "用户访问,缓存套餐分页查询接口")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "page", value = "页码", required = true),
            @ApiImplicitParam(name = "size", value = "每页数量", required = true),
    })
    public Msg<List<Category>> list(Integer page, Integer size){
        // 条件构造器
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<> ();
        // 排序
        lqw.orderByAsc(Category::getSort);
        // 查询
        List<Category> list = srv.list(lqw);

        return Msg.success(Codes.OK,"ok",list);
    }

    /**
     * 条件查询列表
     * @param category
     * @return
     */
    @GetMapping("/find")
    public Msg<List<Category>> find(Category category) {
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        // type 条件
        lqw.eq(category.getType() != null,Category::getType,category.getType());
        // 排序 优先sort 相同sort 更新时间排序
        lqw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = srv.list(lqw);

        return Msg.success(Codes.OK,"ok",list);
    }

    /**
     * 添加分类
     * @param category
     * @return
     */
    @PostMapping("")
    public Msg<String> add(@RequestBody Category category) {
        srv.save(category);
        return Msg.success(Codes.OK,"ok","successful");
    }

    /**
     * 修改分类信息
     * @param category
     * @return
     */
    @PutMapping("")
    public Msg<String> edit(@RequestBody Category category) {
        log.info("c:{}",category);
        srv.updateById(category);
        return Msg.success(Codes.OK,"ok","ok");
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public Msg<String> delete(@PathVariable("id") String id) {
        // 直接删除
        // srv.removeById(id);
        // 判断是否关联品牌 商品 和套餐
        // 调用service方法
        srv.remove(id);
        return Msg.success(Codes.OK,"删除成功!",id);
    }
}

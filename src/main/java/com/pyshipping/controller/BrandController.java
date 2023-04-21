package com.pyshipping.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.common.pwd.RandomCode;
import com.pyshipping.model.Brand;
import com.pyshipping.service.BrandSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/brands")
public class BrandController {
    @Autowired
    private BrandSrv srv;

    /**
     * 添加品牌
     * @param brand
     * @return
     */
    @PostMapping
    public Msg<Brand> add(@RequestBody Brand brand) {
        srv.save(brand);
        return Msg.success(Codes.OK, "ok", brand);
    }

    /**
     * 品牌分页数据
     * @param page
     * @param size
     * @param categoryId
     * @return
     */
    @GetMapping
    public Msg<Page> list(Integer page, Integer size, String categoryId) {
        // 分页构造器
        Page<Brand> limited = new Page<>(page, size);
        // 条件构造器
        LambdaQueryWrapper<Brand> lqw = new LambdaQueryWrapper<>();
        // like 参数自动判断 是否添加该条件参与查询
        lqw.like(!StringUtils.isEmpty(categoryId), Brand::getCategoryId, categoryId);
        // 排序
        lqw.orderByAsc(Brand::getSort);
        // 查询
        srv.page(limited, lqw);
        return Msg.success(Codes.OK, "ok", limited);
    }


    /**
     * 条件查询列表
     * @param brand
     * @return
     */
    @GetMapping("/find")
    public Msg<List<Brand>> find(Brand brand) {
        LambdaQueryWrapper<Brand> lqw = new LambdaQueryWrapper<>();
        // 根据分类ID查询
        lqw.eq(brand.getCategoryId() != null,Brand::getCategoryId,brand.getCategoryId());
        // 排序
        lqw.orderByAsc(Brand::getSort).orderByDesc(Brand::getUpdateTime);
        List<Brand> list = srv.list(lqw);
        return Msg.success(Codes.OK,"ok",list);
    }


    /**
     * 已改为公用上传下载方法 UploadController
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Msg<String> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Msg.error(Codes.InvalidArgument, "图片数据不存在", "file empty or null");
        }
        // 接收到的file为系统缓存文件 转存为固定文件
        String fileType = file.getContentType();
        String oldFileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        //判断格式
        if (!(Objects.equals(fileType, "image/png") || Objects.equals(fileType, "image/jpeg") || Objects.equals(fileType, "image/jpg"))) {
            return Msg.error(Codes.InvalidArgument, "只能是图片格式", "file type error");
        }
        // 判断大小
        if (fileSize > 1000 * 1000 * 2) {
            return Msg.error(Codes.InvalidArgument, "图片不能大于2M", "file size error");
        }
        // 重新命名
        assert oldFileName != null;
        String extension = oldFileName.substring(oldFileName.lastIndexOf("."));
        String randomCode = RandomCode.randomString(4);
        String newName = Long.toString(System.currentTimeMillis()) + randomCode + extension;

        // 判断文件夹是否存在
        String logosDir = "src/main/resources/static/logos";
        File saveDir = new File(logosDir);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        String canonicalPath = "";
        try {
            canonicalPath = saveDir.getCanonicalPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String filePath =  canonicalPath + "/" + newName;
        // 保存文件
        try {
            log.info(filePath);
            file.transferTo(new File(filePath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Msg.success(Codes.OK, "ok", "/static/logos/"+newName);
    }

    /**
     * 修改品牌
     * @param brand
     * @return
     */
    @PutMapping
    public Msg<Brand> edit(@RequestBody Brand brand) {
        srv.updateById(brand);
        return Msg.success(Codes.OK,"ok",brand);
    }

    /**
     * 删除品牌
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public Msg<String> delete(@PathVariable String id) {
        // 查询数据
        LambdaQueryWrapper<Brand> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Brand::getId,id);
        Brand brand = srv.getOne(lqw);
        // 删除图片
        if (!Objects.equals(brand.getImage(), "")) {
            File image = new File(UploadController.basePath + brand.getImage());
            if (image.isFile() && image.exists()) {
                if (image.delete()) {
                    log.info("logo:{},已删除",image);
                }
            }
        }
        srv.remove(id);
        return Msg.success(Codes.OK,"删除成功!",id);
    }
}

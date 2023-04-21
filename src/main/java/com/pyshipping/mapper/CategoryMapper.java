package com.pyshipping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pyshipping.model.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类数据层
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}

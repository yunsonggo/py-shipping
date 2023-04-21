package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.mapper.GoodsSpecsMapper;
import com.pyshipping.model.GoodsSpecs;
import com.pyshipping.service.GoodsSpecsSrv;
import org.springframework.stereotype.Service;

@Service
public class GoodsSpecsSrvImpl extends ServiceImpl<GoodsSpecsMapper, GoodsSpecs> implements GoodsSpecsSrv {
}

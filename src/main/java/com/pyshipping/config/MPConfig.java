package com.pyshipping.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis plus 配置项
 */
@Configuration
public class MPConfig {
    // 拦截器
    @Bean
    public MybatisPlusInterceptor mpInterceptor() {
        MybatisPlusInterceptor mpi = new MybatisPlusInterceptor();
        // 添加分页插件
        mpi.addInnerInterceptor(new PaginationInnerInterceptor());
        return mpi;
    }
}

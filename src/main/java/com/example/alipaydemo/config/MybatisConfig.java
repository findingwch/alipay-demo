package com.example.alipaydemo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 配置mybatis
 *
 * @author finding
 * @date 2024/04/02
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.example.alipaydemo.mapper")
public class MybatisConfig {

    /**
     * 引入分页插件 重写拦截器
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        /**
         * PaginationInnerInterceptor:分页插件
         */
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        /**
         * addInnerInterceptor：添加内部拦截器 往里面添加分页插件
         */
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));

        return mybatisPlusInterceptor;
    }

}

package com.example.alipaydemo.base.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.alipaydemo.base.filter.SQLFilter;


import java.util.Map;

/**
 * 查询参数
 *
 * @author finding
 * @date 2024/04/02
 */
public class Query<T> {

    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, false);
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(params.get(Constant.PAGE) != null){
            curPage = Long.parseLong((String)params.get(Constant.PAGE));
        }
        if(params.get(Constant.LIMIT) != null){
            limit = Long.parseLong((String)params.get(Constant.LIMIT));
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
        params.put(Constant.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField = SQLFilter.sqlInject((String)params.get(Constant.ORDER_FIELD));
        String order = (String)params.get(Constant.ORDER);


        //前端字段排序
        if(StringUtils.hasText(orderField) && StringUtils.hasText(order)){
            if(Constant.ASC.equalsIgnoreCase(order)) {
                return  page.addOrder(OrderItem.asc(orderField));
            }else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        //没有排序字段，则不排序
        if(StringUtils.isEmpty(defaultOrderField)){
            return page;
        }

        //默认排序
        if(isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        }else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }
    public IPage<T> getPage(Long pageNum, Long pageSize) {
        return getPage(pageNum,pageSize,null,null);
    }

    public IPage<T> getPage(Long pageNum, Long pageSize, String defaultOrderField, String orderType) {
        return getPage(pageNum,pageSize,defaultOrderField,orderType,false);
    }

    private IPage<T> getPage(Long pageNum, Long pageSize, String defaultOrderField, String orderType, boolean isAsc) {
        //分页参数
        long curPage = 1;
        long limit = 10;

        if(pageNum != null){
            curPage = pageNum;
        }
        if(pageSize!=null){
            limit = pageSize;
        }

        //分页对象
        Page<T> page = new Page<>(curPage, limit);

        //分页参数
//        params.put(Constant.PAGE, page);

        //排序字段
        //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
        String orderField  = SQLFilter.sqlInject(defaultOrderField);

        //前端字段排序
        if(StringUtils.hasText(orderField) && StringUtils.hasText(orderType)){
            if(Constant.ASC.equalsIgnoreCase(orderType)) {
                return  page.addOrder(OrderItem.asc(orderField));
            }else {
                return page.addOrder(OrderItem.desc(orderField));
            }
        }

        //没有排序字段，则不排序
        if(StringUtils.isEmpty(defaultOrderField)){
            return page;
        }

        //默认排序
        if(isAsc) {
            page.addOrder(OrderItem.asc(defaultOrderField));
        }else {
            page.addOrder(OrderItem.desc(defaultOrderField));
        }

        return page;
    }
}

package com.example.alipaydemo.base.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.alipaydemo.base.filter.SQLFilter;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * 查询参数
 *
 * @author finding
 * @date 2024/04/02
 */
public class Query<T> {


    private static final String ASC = "ASC";
    private static final String DESC = "DESC";

    public IPage<T> getPage(Map<String, Object> params) {
        return this.getPage(params, null, true);
    }

    public IPage<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        // 初始化分页参数
        long curPage = getLongParam(params, Constant.PAGE, 1);
        long limit = getLongParam(params, Constant.LIMIT, 10);

        // 创建分页对象
        Page<T> page = new Page<>(curPage, limit);

        // 分页参数
        params.put(Constant.PAGE, page);

        // 处理排序参数
        String orderField = SQLFilter.sqlInject(getStringParam(params, Constant.ORDER_FIELD, ""));
        String order = getStringParam(params, Constant.ORDER, "");

        //没有排序字段，则不排序
        if(StringUtils.isEmpty(defaultOrderField)){
            return page;
        }

        // 前端字段排序优先
        if (StringUtils.hasText(orderField) && StringUtils.hasText(order)) {
            page.addOrder(getOrderItem(orderField, order));
            return page;
        }

        // 默认排序
        page.addOrder(getOrderItem(defaultOrderField, isAsc ? ASC : DESC));

        return page;
    }


    public IPage<T> getPage(Long pageNum, Long pageSize) {
        return getPage(pageNum,pageSize,null,null);
    }

    public IPage<T> getPage(Long pageNum, Long pageSize, String defaultOrderField, String orderType) {
        return getPage(pageNum,pageSize,defaultOrderField,orderType,true);
    }

    public IPage<T> getPage(Long pageNum, Long pageSize, String defaultOrderField, String orderType, boolean isAsc) {
        // 初始化分页参数
        long curPage = pageNum != null ? pageNum : 1;
        long limit = pageSize != null ? pageSize : 10;

        // 创建分页对象
        Page<T> page = new Page<>(curPage, limit);

        //没有排序字段，则不排序
        if(StringUtils.isEmpty(defaultOrderField)){
            return page;
        }

        // 处理排序参数
        String orderField = SQLFilter.sqlInject(defaultOrderField);

        // 前端字段排序优先
        if (StringUtils.hasText(orderField) && StringUtils.hasText(orderType)) {
            page.addOrder(getOrderItem(orderField, orderType));
            return page;
        }

        // 默认排序
        page.addOrder(getOrderItem(defaultOrderField, isAsc ? ASC : DESC));

        return page;
    }

    private OrderItem getOrderItem(String field, String orderType) {
        return ASC.equalsIgnoreCase(orderType) ? OrderItem.asc(field) : OrderItem.desc(field);
    }

    private String getStringParam(Map<String, Object> params, String key, String defaultValue) {
        Object value = params.get(key);
        return value instanceof String ? (String) value : defaultValue;
    }

    private long getLongParam(Map<String, Object> params, String key, long defaultValue) {
        Object value = params.get(key);
        return value instanceof Number ? ((Number) value).longValue() : defaultValue;
    }


}

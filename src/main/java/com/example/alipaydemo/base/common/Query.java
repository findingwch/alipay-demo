package com.example.alipaydemo.base.common;


import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 查询公共参数
 *

 */
@Data
public class Query {
    /**
     * 法典
     */
    String code;
    /**
     * 表名
     */
    String tableName;
    /**
     * ATTR 类型
     */
    String attrType;
    /**
     * 列类型
     */
    String columnType;
    /**
     * conn 名称
     */
    String connName;
    /**
     * DB 类型
     */
    String dbType;
    /**
     * 项目名称
     */
    String projectName;

    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    Integer page;

    @NotNull(message = "每页条数不能为空")
    @Range(min = 1, max = 1000, message = "每页条数，取值范围 1-1000")
    Integer limit;
}

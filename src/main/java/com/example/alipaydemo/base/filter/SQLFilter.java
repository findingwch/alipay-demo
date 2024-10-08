package com.example.alipaydemo.base.filter;
import org.springframework.util.StringUtils;

import java.sql.SQLException;


/**
 * sqlfilter
 *
 * @author finding
 * @date 2024/04/23
 */
public class SQLFilter {

    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     */
    public static String sqlInject(String str)  {
        if(StringUtils.isEmpty(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alter", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if (str.contains(keyword)) {
                try {
                    throw new SQLException();
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }

        return str;
    }
}

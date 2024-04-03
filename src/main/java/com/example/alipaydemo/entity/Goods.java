package com.example.alipaydemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "goods")
public class Goods {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "products_id")
    private Long productsId;

    /**
     * 名称
     */
    @TableField(value = "products_name")
    private String productsName;

    /**
     * 单价
     */
    @TableField(value = "price")
    private BigDecimal price;

    /**
     * 库存
     */
    @TableField(value = "store_num")
    private Long storeNum;

    /**
     * 封面
     */
    @TableField(value = "cover_img")
    private String coverImg;
}
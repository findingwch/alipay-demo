package com.example.alipaydemo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

/**
 * 测试2
 *
 * @author finding 
 * @since 1.0.0 2024-04-24
 */

@Data
@TableName("gen_test_student")
public class GenTestStudentEntity {
	/**
	* 学生ID
	*/
	@TableId
	private Long id;

	/**
	* 姓名
	*/
	private String name;

	/**
	* 性别
	*/
	private Integer gender;

	/**
	* 年龄
	*/
	private Integer age;

	/**
	* 班级
	*/
	private String className;

	/**
	* 版本号
	*/
	private Integer version;

	/**
	* 删除标识
	*/
	private Integer deleted;

	/**
	* 创建者
	*/
	private Long creator;

	/**
	* 创建时间
	*/
	private LocalDateTime createTime;

	/**
	* 更新者
	*/
	private Long updater;

	/**
	* 更新时间
	*/
	private LocalDateTime updateTime;

}
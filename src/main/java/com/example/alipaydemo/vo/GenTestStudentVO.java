package com.example.alipaydemo.vo;

import com.example.alipaydemo.base.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
* 测试2
*
* @author finding 
* @since 1.0.0 2024-04-24
*/
@Data
public class GenTestStudentVO implements Serializable {
	private static final long serialVersionUID = 1L;


		/**
		* 学生ID
		*/
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
		@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private LocalDateTime createTime;


		/**
		* 更新者
		*/
	private Long updater;


		/**
		* 更新时间
		*/
		@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private LocalDateTime updateTime;


}
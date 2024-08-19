package com.example.alipaydemo.service;


import com.example.alipaydemo.base.common.PageResult;
import com.example.alipaydemo.entity.GenTestStudentEntity;

import com.example.alipaydemo.vo.GenTestStudentVO;
import com.example.alipaydemo.vo.query.GenTestStudentQuery;


import java.util.List;

/**
 * 测试2
 *
 * @author finding
 * @since 1.0.0 2024-04-24
 */
public interface GenTestStudentService extends BaseService<GenTestStudentEntity> {

    PageResult<GenTestStudentVO> page(GenTestStudentQuery query);

    void save(GenTestStudentVO vo);

    void update(GenTestStudentVO vo);

    void delete(List<Long> idList);
}
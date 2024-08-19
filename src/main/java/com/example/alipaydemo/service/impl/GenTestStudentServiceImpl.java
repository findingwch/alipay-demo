package com.example.alipaydemo.service.impl;



import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.alipaydemo.base.common.PageResult;
import com.example.alipaydemo.mapper.GenTestStudentDao;
import com.example.alipaydemo.vo.query.GenTestStudentQuery;
import lombok.AllArgsConstructor;
import com.example.alipaydemo.convert.GenTestStudentConvert;
import com.example.alipaydemo.entity.GenTestStudentEntity;

import com.example.alipaydemo.vo.GenTestStudentVO;

import com.example.alipaydemo.service.GenTestStudentService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 测试2
 *
 * @author finding 
 * @since 1.0.0 2024-04-24
 */
@Service
@AllArgsConstructor
public class GenTestStudentServiceImpl extends BaseServiceImpl<GenTestStudentDao, GenTestStudentEntity> implements GenTestStudentService {

    @Override
    public PageResult<GenTestStudentVO> page(GenTestStudentQuery query) {
        IPage<GenTestStudentEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        return new PageResult<>(GenTestStudentConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<GenTestStudentEntity> getWrapper(GenTestStudentQuery query){
        LambdaQueryWrapper<GenTestStudentEntity> wrapper = Wrappers.lambdaQuery();
        return wrapper;
    }

    @Override
    public void save(GenTestStudentVO vo) {
        GenTestStudentEntity entity = GenTestStudentConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);
    }

    @Override
    public void update(GenTestStudentVO vo) {
        GenTestStudentEntity entity = GenTestStudentConvert.INSTANCE.convert(vo);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

}
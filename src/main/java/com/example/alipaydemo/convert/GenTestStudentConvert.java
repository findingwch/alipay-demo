package com.example.alipaydemo.convert;

import com.example.alipaydemo.entity.GenTestStudentEntity;
import com.example.alipaydemo.vo.GenTestStudentVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
* 测试2
*
* @author finding 
* @since 1.0.0 2024-04-24
*/
@Mapper
public interface GenTestStudentConvert {

    GenTestStudentConvert INSTANCE = Mappers.getMapper(GenTestStudentConvert.class);

    GenTestStudentEntity convert(GenTestStudentVO vo);

    GenTestStudentVO convert(GenTestStudentEntity entity);

    List<GenTestStudentVO> convertList(List<GenTestStudentEntity> list);

}
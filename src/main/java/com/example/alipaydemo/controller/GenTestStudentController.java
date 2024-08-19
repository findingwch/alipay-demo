package com.example.alipaydemo.controller;


import com.example.alipaydemo.base.common.PageResult;
import com.example.alipaydemo.base.common.Result;
import com.example.alipaydemo.convert.GenTestStudentConvert;
import com.example.alipaydemo.entity.GenTestStudentEntity;
import com.example.alipaydemo.service.GenTestStudentService;
import com.example.alipaydemo.vo.GenTestStudentVO;
import com.example.alipaydemo.vo.query.GenTestStudentQuery;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 测试2
 *
 * @author finding
 * @since 1.0.0 2024-04-24
 */
@RestController
@RequestMapping("system/gen_test_student")
@RequiredArgsConstructor
public class GenTestStudentController {
    private final GenTestStudentService genTestStudentService;

    @GetMapping("page")
    public Result<PageResult<GenTestStudentVO>> page(@ParameterObject @Validated GenTestStudentQuery query) {
        PageResult<GenTestStudentVO> page = genTestStudentService.page(query);
        return Result.ok(page);
    }

    @GetMapping("{id}")
    public Result<GenTestStudentVO> get(@PathVariable("id") Long id) {
        GenTestStudentEntity entity = genTestStudentService.getById(id);
        return Result.ok(GenTestStudentConvert.INSTANCE.convert(entity));
    }

    @PostMapping
    public Result<String> save(@RequestBody GenTestStudentVO vo) {
        genTestStudentService.save(vo);
        return Result.ok();
    }

    @PutMapping
    public Result<String> update(@RequestBody @Valid GenTestStudentVO vo) {

        genTestStudentService.update(vo);
        return Result.ok();
    }

    @DeleteMapping
    public Result<String> delete(@RequestBody List<Long> idList) {
        genTestStudentService.delete(idList);
        return Result.ok();
    }
}
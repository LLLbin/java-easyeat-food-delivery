package com.lllebin.controller;

import com.lllebin.dto.DishDto;
import com.lllebin.dto.SetmealDto;
import com.lllebin.response.CommonResponse;
import com.lllebin.response.PageResponse;
import com.lllebin.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: SetmealController
 * Package: com.lllebin.controller
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @PostMapping
    public CommonResponse<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("新增套餐， {}", setmealDto);
        setmealService.save(setmealDto);
        return CommonResponse.success("新增套餐成功");
    }

    @GetMapping("/page")
    public CommonResponse<PageResponse<SetmealDto>> page(int page, int pageSize, String name) {
        log.info("分页查询菜品， page = {}， pageSize = {}， name = {}", page, pageSize, name);
        PageResponse<SetmealDto> setmealDtoPageResponse = setmealService.getPage(page, pageSize, name);
        return CommonResponse.success(setmealDtoPageResponse);
    }

    @GetMapping("/{id}")
    public CommonResponse<SetmealDto> getSetmealDtoById(@PathVariable Long id) {
        log.info("根据Id查询SetmealDto， {}", id);
        SetmealDto setmealDto = setmealService.getSetmealDtoById(id);
        return CommonResponse.success(setmealDto);
    }

    @PutMapping
    public CommonResponse<String> update(@RequestBody SetmealDto setmealDto) {
        log.info("修改套餐， {}", setmealDto);
        setmealService.update(setmealDto);
        return CommonResponse.success("修改套餐成功");
    }

    @PutMapping("/status/{status}")
    public CommonResponse<String> updateStatus(@PathVariable int status, @RequestParam List<Long> ids) {
        log.info("修改套餐状态， status = {}, ids = {}", status, ids);
        setmealService.updateStatusById(status, ids);
        return CommonResponse.success("修改菜品状态成功");
    }

    @DeleteMapping
    public CommonResponse<String> deleteById(@RequestParam List<Long> ids) {
        log.info("删除菜品， {}", ids);
        setmealService.deleteById(ids);
        return CommonResponse.success("删除菜品成功");
    }
}

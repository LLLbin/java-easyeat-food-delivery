package com.lllebin.controller;

import com.lllebin.domain.Dish;
import com.lllebin.dto.DishDto;
import com.lllebin.response.CommonResponse;
import com.lllebin.response.PageResponse;
import com.lllebin.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ClassName: DishController
 * Package: com.lllebin.controller
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping
    public CommonResponse<String> save(@RequestBody DishDto dishDto) {
        log.info("新增菜品， {}", dishDto);
        dishService.save(dishDto);
        return CommonResponse.success("新增菜品成功");
    }

    @GetMapping("/page")
    public CommonResponse<PageResponse<DishDto>> page(int page, int pageSize, String name) {
        log.info("分页查询菜品， page = {}, pageSize = {}, name = {}", page, pageSize, name);
        PageResponse<DishDto> dishPageResponse = dishService.pageQuery(page, pageSize, name);
        return CommonResponse.success(dishPageResponse);
    }

    @GetMapping("/{id}")
    public CommonResponse<DishDto> getDishDtoById(@PathVariable Long id){
        log.info("根据Id查询DishDto， {}", id);
        DishDto dishDto = dishService.getDishDtoById(id);
        return CommonResponse.success(dishDto);
    }

    @PutMapping
    public CommonResponse<String> update(@RequestBody DishDto dishDto) {
        log.info("修改菜品， {}", dishDto);
        dishService.update(dishDto);
        return CommonResponse.success("新增菜品成功");
    }


    @PutMapping ("/status")
    public CommonResponse<String> updateStatus(@RequestBody DishDto dishDto) {
        log.info("修改菜品状态， {}", dishDto);
        dishService.updateStatus(dishDto);
        return CommonResponse.success("修改菜品状态成功");
    }

    @DeleteMapping
    public CommonResponse<String> deleteById(Long id) {
        log.info("删除菜品， {}", id);
        dishService.delteById(id);
        return CommonResponse.success("删除菜品成功");
    }

}

package com.lllebin.controller;

import com.lllebin.dto.DishDto;
import com.lllebin.response.CommonResponse;
import com.lllebin.response.PageResponse;
import com.lllebin.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @CacheEvict(value = "dishCache", allEntries = true)
    public CommonResponse<String> save(@RequestBody DishDto dishDto) {
        log.info("新增菜品， {}", dishDto);
        dishService.save(dishDto);
        return CommonResponse.success("新增菜品成功");
    }

    @GetMapping("/page")
    public CommonResponse<PageResponse<DishDto>> page(int page, int pageSize, String name) {
        log.info("分页查询菜品， page = {}, pageSize = {}, name = {}", page, pageSize, name);
        PageResponse<DishDto> dishPageResponse = dishService.getPage(page, pageSize, name);
        return CommonResponse.success(dishPageResponse);
    }

    @GetMapping("/{id}")
    public CommonResponse<DishDto> getDishDtoById(@PathVariable Long id){
        log.info("根据Id查询DishDto， {}", id);
        DishDto dishDto = dishService.getDishDtoById(id);
        return CommonResponse.success(dishDto);
    }

    @GetMapping("/list")
    @Cacheable(value = "dishCache", key = "#dishDto.categoryId + '_' + #dishDto.status")
    public CommonResponse<List<DishDto>> listByCategoryId(DishDto dishDto) {
        log.info("根据categoryId查询DishDto， {}", dishDto);
        List<DishDto> dishDtoList = dishService.listByCategoryId(dishDto.getCategoryId());
        return CommonResponse.success(dishDtoList);
    }

    @PutMapping
    @CacheEvict(value = "dishCache", allEntries = true)
    public CommonResponse<String> update(@RequestBody DishDto dishDto) {
        log.info("修改菜品， {}", dishDto);
        dishService.update(dishDto);
        return CommonResponse.success("新增菜品成功");
    }


    @PutMapping ("/status/{status}")
    @CacheEvict(value = "dishCache", allEntries = true)
    public CommonResponse<String> updateStatus(@PathVariable int status, @RequestParam List<Long> ids) {
        log.info("修改菜品状态， status = {}, ids = {}", status, ids);
        dishService.updateStatusById(status, ids);
        return CommonResponse.success("修改菜品状态成功");
    }

    @DeleteMapping
    @CacheEvict(value = "dishCache", allEntries = true)
    public CommonResponse<String> deleteById(@RequestParam List<Long> ids) {
        log.info("删除菜品， {}", ids);
        dishService.delteById(ids);
        return CommonResponse.success("删除菜品成功");
    }

}

package com.lllebin.controller;

import com.lllebin.domain.Category;
import com.lllebin.response.CommonResponse;
import com.lllebin.response.PageResponse;
import com.lllebin.service.CategoryService;
import com.lllebin.utils.Snowflake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ClassName: CategoryController
 * Package: com.lllebin.controller
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public CommonResponse<String> save(@RequestBody Category category) {
        log.info("新增分类， {}", category);
        categoryService.save(category);
        return CommonResponse.success("新增分类成功");
    }

    @GetMapping("/page")
    public CommonResponse<PageResponse<Category>> page(int page, int pageSize) {
        log.info("查询分类， page = {}, pageSize = {}", page, pageSize);
        PageResponse<Category> categoryPageResponse = categoryService.pageQuery(page, pageSize);
        return CommonResponse.success(categoryPageResponse);
    }

    @DeleteMapping
    public CommonResponse<String> deleteById(Long id) {
        log.info("删除分类， id = {}", id);
        categoryService.deleteById(id);
        return CommonResponse.success("删除分类完成");
    }

    @PutMapping
    public CommonResponse<String> update(@RequestBody Category category) {
        log.info("更新分类，  category = {}", category);
        categoryService.updateById(category);
        return CommonResponse.success("更新分类完成");
    }

    @GetMapping("/list")
    public CommonResponse<List<Category>> list(Category category) {
        log.info("查询指定类型分类， {}", category.getType());
        List<Category> categoryList = categoryService.list(category);
        return CommonResponse.success(categoryList);
    }
}

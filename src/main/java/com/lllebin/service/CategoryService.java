package com.lllebin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lllebin.domain.Category;
import com.lllebin.domain.CategoryExample;
import com.lllebin.exception.ServiceException;
import com.lllebin.exception.ServiceExceptionCode;
import com.lllebin.mapper.CategoryMapper;
import com.lllebin.response.PageResponse;
import com.lllebin.utils.SnowflakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: CategortService
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class CategoryService {

    @Autowired
    private SnowflakeUtils snowflakeUtils;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;


    public void save(Category category) {
        category.setId(snowflakeUtils.nextId());

        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        criteria.andNameEqualTo(category.getName());
        List<Category> categories = categoryMapper.selectByExample(categoryExample);
        if (categories.size() > 0) {
            throw new ServiceException(ServiceExceptionCode.TARGET_ALREADY_EXISTS);
        }
        categoryMapper.insertSelective(category);
    }

    public PageResponse<Category> pageQuery(int page, int pageSize) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort ASC");

        PageHelper.startPage(page, pageSize);
        List<Category> categorieList = categoryMapper.selectByExample(categoryExample);
        Page<Category> p = (Page<Category>) categorieList;
        return new PageResponse<>(p.getTotal(), p.getResult());
    }

    public void deleteById(Long id) {

        int count1 = dishService.countCategoryId(id);
        if (count1 > 0) {
            throw new ServiceException(ServiceExceptionCode.TARGET_IS_ASSOCITED);
        }

        int count2 = setmealService.countCategoryId(id);
        if (count2 > 0) {
            throw new ServiceException(ServiceExceptionCode.TARGET_IS_ASSOCITED);
        }

        categoryMapper.deleteByPrimaryKey(id);
    }

    public void updateById(Category category) {
        categoryMapper.updateByPrimaryKeySelective(category);
    }

    public List<Category> listBytype(Category category) {
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort ASC, update_time DESC");

        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (category.getType() == null) {
            throw new ServiceException(ServiceExceptionCode.TARGET_NOT_EXISTS);
        }
        criteria.andTypeEqualTo(category.getType());

        return categoryMapper.selectByExample(categoryExample);
    }

    public List<Category> list() {
        return categoryMapper.selectByExample(null);
    }
}

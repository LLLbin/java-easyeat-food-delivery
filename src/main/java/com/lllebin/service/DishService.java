package com.lllebin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lllebin.domain.Dish;
import com.lllebin.domain.DishExample;
import com.lllebin.domain.DishFlavor;
import com.lllebin.dto.DishDto;
import com.lllebin.exception.ServiceException;
import com.lllebin.exception.ServiceExceptionCode;
import com.lllebin.mapper.DishMapper;
import com.lllebin.response.PageResponse;
import com.lllebin.utils.SnowflakeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: DishService
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class DishService {

    @Autowired
    private SnowflakeUtils snowflakeUtils;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private SetmealDishService setmealDishService;

    public int countCategoryId(Long categoryId) {
        DishExample dishExample = new DishExample();
        DishExample.Criteria criteria = dishExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        return (int) dishMapper.countByExample(dishExample);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void save(DishDto dishDto) {
        // 新增菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        long dishId = snowflakeUtils.nextId();
        dish.setId(dishId);
        dishMapper.insertSelective(dish);

        // 批量新增菜品口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
                dishFlavorService.save(flavor);
            }
        }

    }

    public PageResponse<DishDto> getPage(int page, int pageSize, String name) {
        PageHelper.startPage(page, pageSize);
        Page<DishDto> p = (Page<DishDto>) dishMapper.selectPage(name);
        return new PageResponse<>(p.getTotal(), p.getResult());
    }

    public DishDto getDishDtoById(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = dishMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(dish, dishDto);

        List<DishFlavor> dishFlavorList = dishFlavorService.getDishFlavorByDishId(dish.getId());
        dishDto.setFlavors(dishFlavorList);

        return dishDto;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void update(DishDto dishDto) {
        // 更新菜品信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        dishMapper.updateByPrimaryKey(dish);

        // 批量更新菜品口味信息（先删除再重新添加）
        dishFlavorService.deleteByDishId(dish.getId());
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
                dishFlavorService.save(flavor);
            }
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void updateStatusById(int status, List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            Dish dish = new Dish();
            dish.setStatus(status);
            ids.forEach((id) -> {
                dish.setId(id);
                dishMapper.updateByPrimaryKeySelective(dish);
            });
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void delteById(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            for (Long id : ids) {
                // 如果包含在套餐里面则无法删除
                if (setmealDishService.countDishId(id) > 0) {
                    throw new ServiceException(ServiceExceptionCode.TARGET_IS_ASSOCITED);
                }
                // 如果在售出则无法删除
                DishDto dishDtoById = getDishDtoById(id);
                if (dishDtoById.getStatus() == 1) {
                    throw new ServiceException(ServiceExceptionCode.TARGET_IS_SELLING);
                }
                // 删除菜品信息
                dishMapper.deleteByPrimaryKey(id);
                // 删除菜品口味信息
                dishFlavorService.deleteByDishId(id);
            }
        }
    }

    public List<DishDto> listByCategoryId(Long categoryId) {
        DishExample dishExample = new DishExample();
        DishExample.Criteria criteria = dishExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        criteria.andStatusEqualTo(1);
        dishExample.setOrderByClause("sort ASC, update_time DESC");
        List<Dish> dishList = dishMapper.selectByExample(dishExample);

        List<DishDto> dishDtoList = new ArrayList<>();
        dishList.forEach((dish) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            List<DishFlavor> dishFlavorList = dishFlavorService.getDishFlavorByDishId(dish.getId());
            dishDto.setFlavors(dishFlavorList);
            dishDtoList.add(dishDto);
        });
        return dishDtoList;
    }
}

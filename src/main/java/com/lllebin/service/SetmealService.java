package com.lllebin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lllebin.domain.*;
import com.lllebin.dto.SetmealDto;
import com.lllebin.exception.ServiceException;
import com.lllebin.exception.ServiceExceptionCode;
import com.lllebin.mapper.SetmealMapper;
import com.lllebin.response.PageResponse;
import com.lllebin.utils.SnowflakeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: Setmeal
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class SetmealService {

    @Autowired
    private SnowflakeUtils snowflakeUtils;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishService setmealDishService;

    public int countCategoryId(Long categoryId) {
        SetmealExample setmealExample = new SetmealExample();
        SetmealExample.Criteria criteria = setmealExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        return (int) setmealMapper.countByExample(setmealExample);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void save(SetmealDto setmealDto) {
        // 新增套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal);
        long setmealId = snowflakeUtils.nextId();
        setmeal.setId(setmealId);
        setmealMapper.insertSelective(setmeal);

        // 新增套餐和菜品的关系
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach((setmealDish) -> {
                setmealDish.setSetmealId(setmealId);
                setmealDishService.save(setmealDish);
            });
        }
    }

    public PageResponse<SetmealDto> getPage(int page, int pageSize, String name) {
        PageHelper.startPage(page, pageSize);
        Page<SetmealDto> p = (Page<SetmealDto>) setmealMapper.selectPage(name);
        return new PageResponse<>(p.getTotal(), p.getResult());
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void update(SetmealDto setmealDto) {
        // 更新套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal);
        setmealMapper.updateByPrimaryKey(setmeal);

        // 更新套餐-菜品信息(先删除再重新添加)
        setmealDishService.deleteBySetmealId(setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        if (setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach((setmealDish -> {
                setmealDish.setSetmealId(setmeal.getId());
                setmealDishService.save(setmealDish);
            }));
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void updateStatusById(int status, List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            ids.forEach((id) -> {
                setmeal.setId(id);
                setmealMapper.updateByPrimaryKeySelective(setmeal);
            });
        }
    }

    public SetmealDto getSetmealDtoById(Long id) {
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = setmealMapper.selectByPrimaryKey(id);
        BeanUtils.copyProperties(setmeal, setmealDto);

        List<SetmealDish> setmealDishList = setmealDishService.getSetmealDishBySetmealId(setmeal.getId());
        setmealDto.setSetmealDishes(setmealDishList);

        return setmealDto;
    }

    public List<SetmealDto> listBySetmealDtoId(Long categoryId) {
        SetmealExample setmealExample = new SetmealExample();
        SetmealExample.Criteria criteria = setmealExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId)
                .andStatusEqualTo(1);
        setmealExample.setOrderByClause("update_time DESC");
        List<Setmeal> setmealList = setmealMapper.selectByExample(setmealExample);

        List<SetmealDto> setmealDtoList = new ArrayList<>();
        setmealList.forEach((setmeal) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal, setmealDto);
            setmealDtoList.add(setmealDto);
        });
        return setmealDtoList;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteById(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            for (Long id : ids) {
                // 如果在售出则无法删除
                SetmealDto setmealDto = getSetmealDtoById(id);
                if (setmealDto.getStatus() == 1) {
                    throw new ServiceException(ServiceExceptionCode.TARGET_IS_SELLING);
                }
                // 删除setmeal
                setmealMapper.deleteByPrimaryKey(id);
                // 删除setmeal_dish
                setmealDishService.deleteBySetmealId(id);
            }
        }
    }


}

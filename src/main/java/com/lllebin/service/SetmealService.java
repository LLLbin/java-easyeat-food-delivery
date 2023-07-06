package com.lllebin.service;

import com.lllebin.domain.DishExample;
import com.lllebin.domain.SetmealExample;
import com.lllebin.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private SetmealMapper setmealMapper;

    public int countCategoryId(Long categoryId) {
        SetmealExample setmealExample = new SetmealExample();
        SetmealExample.Criteria criteria = setmealExample.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        return (int) setmealMapper.countByExample(setmealExample);
    }
}

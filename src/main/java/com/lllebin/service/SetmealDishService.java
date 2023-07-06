package com.lllebin.service;

import com.lllebin.domain.SetmealDishExample;
import com.lllebin.mapper.SetmealDishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: SetmealDishService
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class SetmealDishService {

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    public int countDishId(Long id) {
        SetmealDishExample setmealDishExample = new SetmealDishExample();
        SetmealDishExample.Criteria criteria = setmealDishExample.createCriteria();
        criteria.andDishIdEqualTo(id);

        return (int) setmealDishMapper.countByExample(setmealDishExample);
    }
}

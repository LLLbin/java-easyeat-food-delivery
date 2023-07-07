package com.lllebin.service;

import com.lllebin.domain.SetmealDish;
import com.lllebin.domain.SetmealDishExample;
import com.lllebin.domain.SetmealExample;
import com.lllebin.mapper.SetmealDishMapper;
import com.lllebin.utils.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private Snowflake snowflake;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    public int countDishId(Long id) {
        SetmealDishExample setmealDishExample = new SetmealDishExample();
        SetmealDishExample.Criteria criteria = setmealDishExample.createCriteria();
        criteria.andDishIdEqualTo(id);

        return (int) setmealDishMapper.countByExample(setmealDishExample);
    }

    public void save(SetmealDish setmealDish) {
        setmealDish.setId(snowflake.nextId());
        setmealDishMapper.insertSelective(setmealDish);
    }

    public void deleteBySetmealId(Long setmealId) {
        SetmealDishExample setmealDishExample = new SetmealDishExample();
        SetmealDishExample.Criteria criteria = setmealDishExample.createCriteria();
        criteria.andSetmealIdEqualTo(setmealId);

        setmealDishMapper.deleteByExample(setmealDishExample);
    }

    public List<SetmealDish> getSetmealDishBySetmealId(Long setmealId) {
        SetmealDishExample setmealDishExample = new SetmealDishExample();
        SetmealDishExample.Criteria criteria = setmealDishExample.createCriteria();
        criteria.andSetmealIdEqualTo(setmealId);
        return setmealDishMapper.selectByExample(setmealDishExample);
    }
}

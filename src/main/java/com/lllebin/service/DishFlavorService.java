package com.lllebin.service;

import com.lllebin.domain.DishFlavor;
import com.lllebin.domain.DishFlavorExample;
import com.lllebin.mapper.DishFlavorMapper;
import com.lllebin.utils.SnowflakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: DishFlavorService
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class DishFlavorService {

    @Autowired
    private SnowflakeUtils snowflakeUtils;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    public void save(DishFlavor flavor) {
        flavor.setId(snowflakeUtils.nextId());
        dishFlavorMapper.insertSelective(flavor);
    }

    public List<DishFlavor> getDishFlavorByDishId(Long dishId) {
        DishFlavorExample dishFlavorExample = new DishFlavorExample();
        DishFlavorExample.Criteria criteria = dishFlavorExample.createCriteria();
        criteria.andDishIdEqualTo(dishId);
        return dishFlavorMapper.selectByExample(dishFlavorExample);
    }

    public void deleteByDishId(Long dishId) {
        DishFlavorExample dishFlavorExample = new DishFlavorExample();
        DishFlavorExample.Criteria criteria = dishFlavorExample.createCriteria();
        criteria.andDishIdEqualTo(dishId);

        dishFlavorMapper.deleteByExample(dishFlavorExample);
    }
}

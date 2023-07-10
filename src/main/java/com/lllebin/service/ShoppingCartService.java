package com.lllebin.service;

import com.lllebin.domain.ShoppingCart;
import com.lllebin.domain.ShoppingCartExample;
import com.lllebin.mapper.ShoppingCartMapper;
import com.lllebin.utils.BaseContextUtils;
import com.lllebin.utils.SnowflakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: ShoppingCartService
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private SnowflakeUtils snowflakeUtils;

    public ShoppingCart add(ShoppingCart shoppingCart) {

        // 1. 确定用户Id
        shoppingCart.setUserId(BaseContextUtils.getCurrentId());

        // 2. 判断菜品/套餐是否已存在
        ShoppingCartExample shoppingCartExample = new ShoppingCartExample();
        ShoppingCartExample.Criteria criteria = shoppingCartExample.createCriteria();
        List<ShoppingCart> shoppingCartList;
        if (shoppingCart.getDishId() != null) {
            // 2-1. 菜品操作
            criteria.andUserIdEqualTo(shoppingCart.getUserId())
                    .andDishIdEqualTo(shoppingCart.getDishId());
            shoppingCartList = shoppingCartMapper.selectByExample(shoppingCartExample);
        } else {
            // 2-2. 套餐操作
            criteria.andUserIdEqualTo(shoppingCart.getUserId())
                    .andDishIdEqualTo(shoppingCart.getSetmealId());
            shoppingCartList = shoppingCartMapper.selectByExample(shoppingCartExample);
        }

        // 3. 存在则数量+1，否则插入新数据
        if (shoppingCartList.size() > 0) {
            ShoppingCart tmp = shoppingCartList.get(0);
            tmp.setNumber(tmp.getNumber() + 1);
            shoppingCart = tmp;
            shoppingCartMapper.updateByPrimaryKeySelective(shoppingCart);
        } else {
            shoppingCart.setId(snowflakeUtils.nextId());
            shoppingCart.setNumber(1);
            shoppingCartMapper.insertSelective(shoppingCart);
        }

        return shoppingCart;
    }

    public ShoppingCart sub(ShoppingCart shoppingCart) {
        // 1. 确定用户Id
        shoppingCart.setUserId(BaseContextUtils.getCurrentId());

        // 2. 判断菜品/套餐数量是否大于1
        ShoppingCartExample shoppingCartExample = new ShoppingCartExample();
        ShoppingCartExample.Criteria criteria = shoppingCartExample.createCriteria();
        List<ShoppingCart> shoppingCartList;
        if (shoppingCart.getDishId() != null) {
            // 2-1. 菜品操作
            criteria.andUserIdEqualTo(shoppingCart.getUserId())
                    .andDishIdEqualTo(shoppingCart.getDishId());
            shoppingCartList = shoppingCartMapper.selectByExample(shoppingCartExample);
        } else {
            // 2-2. 套餐操作
            criteria.andUserIdEqualTo(shoppingCart.getUserId())
                    .andDishIdEqualTo(shoppingCart.getSetmealId());
            shoppingCartList = shoppingCartMapper.selectByExample(shoppingCartExample);
        }

        // 3. 菜品数量大于1就减1，否则删除数据
        shoppingCart = shoppingCartList.get(0);
        if (shoppingCart.getNumber() > 1) {
            shoppingCart.setNumber(shoppingCart.getNumber() - 1);
            shoppingCartMapper.updateByPrimaryKeySelective(shoppingCart);
        } else {
            shoppingCart.setNumber(0);
            shoppingCartMapper.deleteByPrimaryKey(shoppingCart.getId());
        }
        return shoppingCart;
    }

    public List<ShoppingCart> listByUserId() {
        ShoppingCartExample shoppingCartExample = new ShoppingCartExample();
        ShoppingCartExample.Criteria criteria = shoppingCartExample.createCriteria();
        criteria.andUserIdEqualTo(BaseContextUtils.getCurrentId());
        return shoppingCartMapper.selectByExample(shoppingCartExample);
    }

    public void deleteByUserId() {
        ShoppingCartExample shoppingCartExample = new ShoppingCartExample();
        ShoppingCartExample.Criteria criteria = shoppingCartExample.createCriteria();
        criteria.andUserIdEqualTo(BaseContextUtils.getCurrentId());
        shoppingCartMapper.deleteByExample(shoppingCartExample);
    }
}

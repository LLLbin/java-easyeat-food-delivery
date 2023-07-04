package com.lllebin.mapper;

import com.lllebin.domain.SetmealDish;
import com.lllebin.domain.SetmealDishExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface SetmealDishMapper {
    long countByExample(SetmealDishExample example);

    int deleteByExample(SetmealDishExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SetmealDish row);

    int insertSelective(SetmealDish row);

    List<SetmealDish> selectByExample(SetmealDishExample example);

    SetmealDish selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") SetmealDish row, @Param("example") SetmealDishExample example);

    int updateByExample(@Param("row") SetmealDish row, @Param("example") SetmealDishExample example);

    int updateByPrimaryKeySelective(SetmealDish row);

    int updateByPrimaryKey(SetmealDish row);
}
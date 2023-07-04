package com.lllebin.mapper;

import com.lllebin.domain.DishFlavor;
import com.lllebin.domain.DishFlavorExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface DishFlavorMapper {
    long countByExample(DishFlavorExample example);

    int deleteByExample(DishFlavorExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DishFlavor row);

    int insertSelective(DishFlavor row);

    List<DishFlavor> selectByExample(DishFlavorExample example);

    DishFlavor selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") DishFlavor row, @Param("example") DishFlavorExample example);

    int updateByExample(@Param("row") DishFlavor row, @Param("example") DishFlavorExample example);

    int updateByPrimaryKeySelective(DishFlavor row);

    int updateByPrimaryKey(DishFlavor row);
}
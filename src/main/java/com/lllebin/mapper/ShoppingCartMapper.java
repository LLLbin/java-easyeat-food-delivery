package com.lllebin.mapper;

import com.lllebin.domain.ShoppingCart;
import com.lllebin.domain.ShoppingCartExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ShoppingCartMapper {
    long countByExample(ShoppingCartExample example);

    int deleteByExample(ShoppingCartExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ShoppingCart row);

    int insertSelective(ShoppingCart row);

    List<ShoppingCart> selectByExample(ShoppingCartExample example);

    ShoppingCart selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") ShoppingCart row, @Param("example") ShoppingCartExample example);

    int updateByExample(@Param("row") ShoppingCart row, @Param("example") ShoppingCartExample example);

    int updateByPrimaryKeySelective(ShoppingCart row);

    int updateByPrimaryKey(ShoppingCart row);
}
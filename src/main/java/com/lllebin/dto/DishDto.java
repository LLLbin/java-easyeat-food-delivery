package com.lllebin.dto;

import com.lllebin.domain.Dish;
import com.lllebin.domain.DishFlavor;
import lombok.Data;

import java.util.List;

@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors;

    private String categoryName;

    private Integer copies;
}

package com.lllebin.dto;


import com.lllebin.domain.Setmeal;
import com.lllebin.domain.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

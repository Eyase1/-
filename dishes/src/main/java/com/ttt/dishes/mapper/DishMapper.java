package com.ttt.dishes.mapper;

import com.ttt.entity.dishes.Dish;
import com.ttt.req.dishes.AddDishesReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface    DishMapper {

    List<Dish> getAllDishes();

    void saveDish(AddDishesReq addDishesReq);
}

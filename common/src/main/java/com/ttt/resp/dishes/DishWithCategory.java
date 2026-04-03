package com.ttt.resp.dishes;

import com.ttt.entity.dishes.Dish;
import com.ttt.entity.dishes.DishCategory;
import lombok.Data;

@Data
public class DishWithCategory {
    private Dish dish;
    private DishCategory category;
    private boolean Favorite;
}


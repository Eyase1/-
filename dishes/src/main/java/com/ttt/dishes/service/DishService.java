package com.ttt.dishes.service;

import com.ttt.req.dishes.AddDishesReq;
import com.ttt.resp.dishes.DishWithCategory;
import com.ttt.util.result.Result;

import java.io.IOException;
import java.util.List;

public interface DishService {

    List<DishWithCategory> getAllDishesWithCategories(Long userId);

    void favoriteDish(Long userId, Long dishId);

    void deleteDishById(Long dishId);

    Result addDish(AddDishesReq addDishesReq, String token) throws IOException;
}

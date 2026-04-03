package com.ttt.dishes.controller;

import com.ttt.dishes.service.DishService;
import com.ttt.req.dishes.AddDishesReq;
import com.ttt.resp.dishes.DishWithCategory;
import com.ttt.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class DishController {

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }


    @GetMapping("/{userId}")
    public ResponseEntity<List<DishWithCategory>> getAllDishes(@PathVariable("userId")Long userId) {
        List<DishWithCategory> dishes = dishService.getAllDishesWithCategories(userId);
        return ResponseEntity.ok(dishes);
    }

    /**
     * 收藏或取消收藏菜品
     */
    @PostMapping("/favorite/{dishId}")
    public ResponseEntity<String> favoriteDish(@PathVariable("dishId") Long dishId, @RequestParam Long userId) {
        dishService.favoriteDish(userId, dishId);
        return ResponseEntity.ok("操作成功");
    }

    @DeleteMapping("/{dishId}")
    public Result deleteDishById(@PathVariable("dishId") Long dishId) {
        dishService.deleteDishById(dishId);
        return Result.ok(null);
    }
    @PostMapping("/addDish")
    public Result addDish(@RequestBody AddDishesReq addDishesReq,@RequestHeader("token")String token) throws IOException {

        return dishService.addDish(addDishesReq,token);
    }


}


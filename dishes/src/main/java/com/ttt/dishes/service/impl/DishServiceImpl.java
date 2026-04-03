package com.ttt.dishes.service.impl;

import com.ttt.dishes.feign.IsAdminFeign;
import com.ttt.dishes.mapper.DishCategoryMapper;
import com.ttt.dishes.mapper.DishMapper;
import com.ttt.dishes.mapper.UserFavoriteMapper;
import com.ttt.dishes.service.DishService;
import com.ttt.entity.dishes.Dish;
import com.ttt.entity.dishes.DishCategory;
import com.ttt.entity.user.UserFavorite;
import com.ttt.req.dishes.AddDishesReq;
import com.ttt.resp.dishes.DishWithCategory;
import com.ttt.util.result.Result;
import com.ttt.util.url.BaseUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;
    private final DishCategoryMapper dishCategoryMapper;
    private final UserFavoriteMapper userFavoriteMapper;
    @Autowired
    private IsAdminFeign isAdminFeign;


    @Autowired
    public DishServiceImpl(DishMapper dishMapper,
                           DishCategoryMapper dishCategoryMapper,
                           UserFavoriteMapper userFavoriteMapper) {
        this.dishMapper = dishMapper;
        this.dishCategoryMapper = dishCategoryMapper;
        this.userFavoriteMapper = userFavoriteMapper;
    }

    @Override
    public List<DishWithCategory> getAllDishesWithCategories(Long userId) {
        // 1. 查询所有菜品
        List<Dish> dishes = dishMapper.getAllDishes();
        if (dishes == null || dishes.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 提取所有分类ID
        List<Long> categoryIds = dishes.stream()
                .map(Dish::getCategoryId)
                .distinct()
                .collect(Collectors.toList());

        // 3. 批量查询分类信息
        List<DishCategory> categories = dishCategoryMapper.getCategoriesByIds(categoryIds);

        // 4. 创建分类ID到分类对象的映射
        Map<Long, DishCategory> categoryMap = categories.stream()
                .collect(Collectors.toMap(DishCategory::getId, Function.identity()));

        List<UserFavorite> favorites = userFavoriteMapper.getFavoritesByUserId(userId);
        Map<Long, Boolean> favoriteMap = favorites.stream()
                .collect(Collectors.toMap(UserFavorite::getDishId, f -> true));

        // 5. 组合菜品和分类
        return dishes.stream().filter(dish -> dish.getImageUrl()!=null)
                .map(dish -> {
                    dish.setImageUrl(BaseUrl.imageUrl+dish.getImageUrl());
                    DishWithCategory result = new DishWithCategory();
                    result.setDish(dish);
                    result.setCategory(categoryMap.get(dish.getCategoryId()));
                    result.setFavorite(favoriteMap.getOrDefault(dish.getId(), false));
                    return result;
                })
                .collect(Collectors.toList());
    }

    /**
     * 收藏菜品
     */
    @Override
    public void favoriteDish(Long userId, Long dishId) {
        UserFavorite userFavorite = new UserFavorite();

        userFavorite.setUserId(userId);
        userFavorite.setDishId(dishId);

        // 检查是否已经收藏
        List<UserFavorite> favorites = userFavoriteMapper.getFavoritesByUserId(userId);
        boolean Favorited = favorites.stream()
                .anyMatch(f -> f.getDishId().equals(dishId));

        if (Favorited) {
            // 取消收藏
            userFavoriteMapper.deleteFavorite(userId, dishId);
        } else {
            // 收藏
            userFavoriteMapper.addFavorite(userFavorite);
        }
    }

    @Override
    public void deleteDishById(Long dishId) {
        userFavoriteMapper.deleteDishById(dishId);
    }

    @Override
    public Result addDish(AddDishesReq addDishesReq, String token) throws IOException {
        if(!isAdminFeign.isAdmin(token)){
            return Result.build(null,400,"当前用户无权限");

        }

        if(addDishesReq.getCategory()!=null&&!addDishesReq.getCategory().trim().equals("")){
            Long id=dishCategoryMapper.getIdByName(addDishesReq.getCategory());
            if(id==null){
                dishCategoryMapper.saveCategory(addDishesReq.getCategory());
                id=dishCategoryMapper.getIdByName(addDishesReq.getCategory());
            }
            addDishesReq.setCategoryId(id);
        }
        dishMapper.saveDish(addDishesReq);
        return Result.ok(null);

    }
}


package com.ttt.service;

import com.ttt.entity.dishes.DishesComment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DishesCommentService {

    DishesComment getCommentById(Long id);
    
    List<DishesComment> getCommentsByDishesId(Long dishesId);
    
    List<DishesComment> getCommentsByUserId(Long userId);

    DishesComment getCommentByUserIdAndDishId(Long userId, Long dishId);

    int saveOrUpdateComment(DishesComment comment);

    int deleteComment(Long id, Long userId);

    List<DishesComment> getAllComments();

}
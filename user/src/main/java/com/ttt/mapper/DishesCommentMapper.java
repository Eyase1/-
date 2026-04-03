package com.ttt.mapper;

import com.ttt.entity.dishes.DishesComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishesCommentMapper {
    int insert(DishesComment comment);
    
    int deleteById(Long id);
    
    int update(DishesComment comment);

    // 新增方法：根据用户ID和菜品ID查询评论
    DishesComment selectByUserIdAndDishId(
            @Param("userId") Long userId,
            @Param("dishId") Long dishId
    );

    List<DishesComment> selectAllComments();

    DishesComment selectById(Long id);
    
    List<DishesComment> selectByDishesId(Long dishesId);
    
    List<DishesComment> selectByUserId(Long userId);
}
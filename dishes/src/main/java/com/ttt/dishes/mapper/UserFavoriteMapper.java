package com.ttt.dishes.mapper;

import com.ttt.entity.user.UserFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFavoriteMapper {

    List<UserFavorite> getFavoritesByUserId(@Param("userId") Long userId);
    // 添加收藏
    int addFavorite(UserFavorite userFavorite);

    // 删除收藏
    int deleteFavorite(@Param("userId") Long userId, @Param("dishId") Long dishId);

    void deleteDishById(Long dishId);
}


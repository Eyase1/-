package com.ttt.mapper;

import com.ttt.entity.dishes.Dish;
import com.ttt.resp.dishes.DishWithCategory;
import com.ttt.resp.user.UserCollectionResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCollectionMapper {
    List<UserCollectionResp> getUserCollection(@Param("userId") Integer userId);
}

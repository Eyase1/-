package com.ttt.dishes.mapper;

import com.ttt.entity.dishes.DishCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishCategoryMapper {

    List<DishCategory> getCategoriesByIds(@Param("ids") List<Long> ids);

    Long getIdByName(@Param("name") String category);

    void saveCategory(@Param("name") String category);
}

package com.ttt.mapper;

import com.ttt.entity.user.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentAndDishesInfoMapper {
    // ===== 学生相关 =====
    List<User> getAllStudents();
    Map<String, Object> getStudentById(Long studentId);
    void insertStudent(Map<String, Object> student);
    void updateStudent(User student);
    void deleteStudentById(Long studentId);

    // ===== 菜品相关 =====
    List<Map<String, Object>> getAllDishes();
    Map<String, Object> getDishById(Long dishId);
    void insertDish(Map<String, Object> dish);
    void updateDish(Map<String, Object> dish);
    void deleteDishById(Long dishId);
    void updateDishImageUrl(Long dishId, String imageUrl);

    List<User> search(@Param("info") String info);
}
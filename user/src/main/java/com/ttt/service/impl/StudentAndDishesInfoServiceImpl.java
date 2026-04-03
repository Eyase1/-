package com.ttt.service.impl;

import com.ttt.entity.user.User;
import com.ttt.mapper.StudentAndDishesInfoMapper;
import com.ttt.service.StudentAndDishesInfoService;
import com.ttt.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentAndDishesInfoServiceImpl implements StudentAndDishesInfoService {

    @Autowired
    private StudentAndDishesInfoMapper studentAndDishesInfoMapper;
    @Autowired
    private UserUtil util;
    @Override
    public List<User> getAllStudents(String token) {
        if(!util.isAdmin(token)){
            return List.of();
        }
        return studentAndDishesInfoMapper.getAllStudents();
    }

    @Override
    public Map<String, Object> getStudentById(Long studentId) {
        return studentAndDishesInfoMapper.getStudentById(studentId);
    }

    @Override
    public void addStudent(Map<String, Object> student) {
        studentAndDishesInfoMapper.insertStudent(student);
    }

    @Override
    public void editStudent(User student) {
        studentAndDishesInfoMapper.updateStudent(student);
    }

    @Override
    public void removeStudentById(Long studentId) {
        studentAndDishesInfoMapper.deleteStudentById(studentId);
    }

    @Override
    public List<Map<String, Object>> getAllDishes() {
        return studentAndDishesInfoMapper.getAllDishes();
    }

    @Override
    public Map<String, Object> getDishById(Long dishId) {
        return studentAndDishesInfoMapper.getDishById(dishId);
    }

    @Override
    public void addDish(Map<String, Object> dish) {
        studentAndDishesInfoMapper.insertDish(dish);
    }

    @Override
    public void editDish(Map<String, Object> dish) {
        studentAndDishesInfoMapper.updateDish(dish);
    }

    @Override
    public void removeDishById(Long dishId) {
        studentAndDishesInfoMapper.deleteDishById(dishId);
    }
    @Override
    public void updateDishImageUrl(Long dishId, String imageUrl) {
        studentAndDishesInfoMapper.updateDishImageUrl(dishId, imageUrl);
    }

    @Override
    public List<User> search(String info) {
        info="%"+info+"%";
        List<User>list=studentAndDishesInfoMapper.search(info);
        return list;
    }
}

package com.ttt.controller;

import com.ttt.entity.user.User;
import com.ttt.service.StudentAndDishesInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class StudentAndDishesInfoController {

    @Autowired
    private StudentAndDishesInfoService service;

    // 学生管理
    @GetMapping("/students")
    public List<User> getAllStudents(@RequestHeader("token")String token) {
        return service.getAllStudents(token);
    }
    @PostMapping("/search")
    public List<User> search(@RequestBody String info){
        return service.search(info);
    }

    @GetMapping("/student/{id}")
    public Map<String, Object> getStudent(@PathVariable Long id) {
        return service.getStudentById(id);
    }

    @PostMapping("/student")
    public void createStudent(@RequestBody Map<String, Object> student) {
        service.addStudent(student);
    }

    @PutMapping("/student")
    public void updateStudent(@RequestBody User student) {
        service.editStudent(student);
    }

    @DeleteMapping("/student/{id}")
    public void deleteStudent(@PathVariable Long id) {
        service.removeStudentById(id);
    }

    // 菜品管理
    @GetMapping("/dishes")
    public List<Map<String, Object>> getAllDishes() {
        return service.getAllDishes();
    }

    @GetMapping("/dish/{id}")
    public Map<String, Object> getDish(@PathVariable Long id) {
        return service.getDishById(id);
    }

    @PostMapping("/dish")
    public void createDish(@RequestBody Map<String, Object> dish) {
        service.addDish(dish);
    }

    @PutMapping("/dish")
    public void updateDish(@RequestBody Map<String, Object> dish) {
        service.editDish(dish);
    }

    @DeleteMapping("/dish/{id}")
    public void deleteDish(@PathVariable Long id) {
        service.removeDishById(id);
    }
}

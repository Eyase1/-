package com.ttt.controller;

import com.ttt.entity.dishes.DishesComment;
import com.ttt.service.DishesCommentService;
import com.ttt.util.result.Result;
import com.ttt.util.result.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class DishesCommentController {

    @Autowired
    private DishesCommentService dishesCommentService;

    @PostMapping("/saveComment")
    public Result<Integer> saveComment(@RequestBody DishesComment comment) {
        int result = dishesCommentService.saveOrUpdateComment(comment);
        return result > 0
                ? Result.ok(result)
                : Result.build(0, 403, "评论创建或更新失败", false);
    }

    @DeleteMapping("/{id}")
    public Result<Integer> deleteComment(@PathVariable Long id, @RequestParam Long userId) {
        int result = dishesCommentService.deleteComment(id, userId);
        return result > 0
                ? Result.ok(result)
                : Result.build(0, 403, "无权删除或评论不存在", false);
    }

    @GetMapping("/dishes/allComment")
    public Result<List<DishesComment>> getAllComments() {
        List<DishesComment> comments = dishesCommentService.getAllComments();
        return Result.ok(comments);
    }

    @GetMapping("/{id}")
    public Result<DishesComment> getCommentById(@PathVariable Long id) {
        DishesComment comment = dishesCommentService.getCommentById(id);
        return comment != null
                ? Result.ok(comment)
                : Result.build(null, ResultCodeEnum.USERNAME_ERROR);
    }

    @GetMapping("/dishes/{dishesId}")
    public Result<List<DishesComment>> getCommentsByDishesId(@PathVariable Long dishesId) {
        List<DishesComment> comments = dishesCommentService.getCommentsByDishesId(dishesId);
        return Result.ok(comments);
    }

    @GetMapping("/user/{userId}")
    public Result<List<DishesComment>> getCommentsByUserId(@PathVariable Long userId) {
        List<DishesComment> comments = dishesCommentService.getCommentsByUserId(userId);
        return Result.ok(comments);
    }
}

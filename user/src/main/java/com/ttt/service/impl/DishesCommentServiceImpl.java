package com.ttt.service.impl;

import com.ttt.entity.dishes.DishesComment;
import com.ttt.mapper.DishesCommentMapper;
import com.ttt.service.DishesCommentService;
import com.ttt.util.result.Result;
import com.ttt.util.result.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishesCommentServiceImpl implements DishesCommentService {

    @Autowired
    private DishesCommentMapper dishesCommentMapper;

    @Override
    public DishesComment getCommentById(Long id) {
        return dishesCommentMapper.selectById(id);
    }

    @Override
    public List<DishesComment> getCommentsByDishesId(Long dishesId) {
        return dishesCommentMapper.selectByDishesId(dishesId);
    }

    @Override
    public List<DishesComment> getCommentsByUserId(Long userId) {
        return dishesCommentMapper.selectByUserId(userId);
    }

    @Override
    public int saveOrUpdateComment(DishesComment comment) {
        // 检查是否已存在评论
        DishesComment existing = dishesCommentMapper.selectByUserIdAndDishId(
                comment.getUserId(),
                comment.getDishId()
        );

        if (existing != null) {
            // 更新已有评论
            comment.setId(existing.getId()); // 设置要更新的ID
            existing.setReviewContent(comment.getReviewContent());
            return dishesCommentMapper.update(comment);
        } else {
            // 新增评论
            return dishesCommentMapper.insert(comment);
        }
    }

    @Override
    public DishesComment getCommentByUserIdAndDishId(Long userId, Long dishId) {
        return dishesCommentMapper.selectByUserIdAndDishId(userId, dishId);
    }

    @Override
    public int deleteComment(Long id, Long userId) {
        DishesComment comment = dishesCommentMapper.selectById(id);
        if (comment == null) {
            Result.build(null, 403,"评论不存在", false);
        }
        if (!comment.getUserId().equals(userId)) {
            Result.build(null, 403,"无权修改他人评论", false);
        }
        return dishesCommentMapper.deleteById(id);
    }

    @Override
    public List<DishesComment> getAllComments() {
        return dishesCommentMapper.selectAllComments();
    }

}

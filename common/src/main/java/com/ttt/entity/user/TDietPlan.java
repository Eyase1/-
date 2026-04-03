package com.ttt.entity.user;

import lombok.Data;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
public class TDietPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    // 主键，自动递增
    private BigInteger id;
    // 用户id
    private BigInteger userId;
    // 计划日期
    private Date planDate;
    // 早餐菜品
    private String breakfastDish;
    // 午餐菜品
    private String lunchDish;
    // 晚餐菜品
    private String dinnerDish;
    // 总卡路里
    private String totalCalories;
    // 总蛋白质
    private String totalProtein;
    // 总脂肪
    private String totalFat;
    // 总碳水化合物
    private String totalCarbohydrate;
    // 总纤维
    private String totalFiber;
    // 营养评分
    private String nutritionScore;
    // 状态，默认'active'
    private String status;
    // 创建时间，默认当前时间
    private Date createTime;
}
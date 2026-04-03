package com.ttt.entity.dishes;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DishesComment {
    private Long id;
    private Long userId;
    private Long dishId;
    private String dishName;
    private String reviewContent;
    private BigDecimal rating;
    private Date reviewTime;
}
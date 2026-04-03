package com.ttt.entity.user;

import lombok.Data;

@Data
public class UserFavorite {
    private Long id;
    private Long userId;
    private Long dishId;
    private String createTime;
}


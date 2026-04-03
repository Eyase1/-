package com.ttt.entity.dishes;

import lombok.Data;

@Data
public class DishCategory {
    private Long id;
    private String name;
    private Long parentId;
    private String description;

}


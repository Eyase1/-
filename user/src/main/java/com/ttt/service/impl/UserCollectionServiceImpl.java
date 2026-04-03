package com.ttt.service.impl;

import com.ttt.entity.dishes.Dish;
import com.ttt.mapper.UserCollectionMapper;
import com.ttt.resp.dishes.DishWithCategory;
import com.ttt.resp.user.UserCollectionResp;
import com.ttt.service.UserCollectionService;
import com.ttt.util.result.Result;
import com.ttt.util.url.BaseUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCollectionServiceImpl implements UserCollectionService {
    @Autowired
    private UserCollectionMapper userCollectionMapper;
    @Override
    public Result getUserCollection(Integer userId) {
        List<UserCollectionResp>list=userCollectionMapper.getUserCollection(userId);
        for(UserCollectionResp co:list){
            co.setImageUrl(BaseUrl.imageUrl+co.getImageUrl());
        }
        return Result.ok(list);
    }
}

package com.ttt.controller;

import com.ttt.service.UserCollectionService;
import com.ttt.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
@RestController
@RequestMapping("/collection")
public class UserCollectionController implements Serializable {
    @Autowired
    private UserCollectionService userCollectionService;
    @PostMapping("/{userId}")
    public Result getUserCollection(@PathVariable Integer userId) {
        return userCollectionService.getUserCollection(userId);
    }
}

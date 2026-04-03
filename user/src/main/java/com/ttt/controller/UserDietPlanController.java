package com.ttt.controller;

import com.ttt.entity.user.TDietPlan;
import com.ttt.req.user.UserDietPlanReq;
import com.ttt.service.UserDietPlanService;
import com.ttt.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
public class UserDietPlanController {
    @Autowired
    UserDietPlanService userDietPlanService;
    @PostMapping("/getPlan")
    public Result getPlan(@RequestBody UserDietPlanReq userDietPlanReq){
        if(userDietPlanReq.getPlanDate()==null||userDietPlanReq.getPlanDate().trim().equals("")){
            return Result.ok(null);
        }
        TDietPlan tDietPlan=userDietPlanService.getPlanByUserIdAndDate(userDietPlanReq.getPlanDate(),userDietPlanReq.getUserId());
        return Result.ok(tDietPlan);
    }
    @PostMapping("/saveorupdate")
    public Result saveorupdate(@RequestBody UserDietPlanReq userDietPlanReq){
        return userDietPlanService.saveOrUpdate(userDietPlanReq);
    }
}

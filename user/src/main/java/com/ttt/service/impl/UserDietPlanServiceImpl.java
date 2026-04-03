package com.ttt.service.impl;

import com.ttt.entity.user.TDietPlan;
import com.ttt.mapper.UserDietPlanMapper;
import com.ttt.req.user.UserDietPlanReq;
import com.ttt.service.UserDietPlanService;
import com.ttt.util.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDietPlanServiceImpl implements UserDietPlanService {
    @Autowired
    private UserDietPlanMapper userDietPlanMapper;
    @Override
    public TDietPlan getPlanByUserIdAndDate(String planDate, String userId) {
        TDietPlan tDietPlan=userDietPlanMapper.getPlanByUserIdAndDate(planDate,userId);
        return tDietPlan;
    }

    @Override
    public Result saveOrUpdate(UserDietPlanReq userDietPlanReq) {
        TDietPlan cur=userDietPlanMapper.getPlanByUserIdAndDate(userDietPlanReq.getPlanDate(),userDietPlanReq.getUserId());
        if(cur==null){
            userDietPlanMapper.save(userDietPlanReq);
            return Result.ok(null);
        }
        userDietPlanMapper.update(userDietPlanReq);

        return Result.ok(null);
    }
}

package com.ttt.mapper;

import com.ttt.entity.user.TDietPlan;
import com.ttt.req.user.UserDietPlanReq;
import feign.Param;

public interface UserDietPlanMapper {
    TDietPlan getPlanByUserIdAndDate(@Param("planDate") String planDate, @Param("userId") String userId);

    void save(UserDietPlanReq userDietPlanReq);

    void update(UserDietPlanReq userDietPlanReq);
}

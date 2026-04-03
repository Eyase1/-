package com.ttt.req.user;

import java.io.Serializable;

public class UserDietPlanReq implements Serializable {
    private String userId;
    private String planDate;
    private String breakfastDish;
    private String lunchDish;
    private String dinnerDish;

    public String getBreakfastDish() {
        return breakfastDish;
    }

    public void setBreakfastDish(String breakfastDish) {
        this.breakfastDish = breakfastDish;
    }

    public String getLunchDish() {
        return lunchDish;
    }

    public void setLunchDish(String lunchDish) {
        this.lunchDish = lunchDish;
    }

    public String getDinnerDish() {
        return dinnerDish;
    }

    public void setDinnerDish(String dinnerDish) {
        this.dinnerDish = dinnerDish;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlanDate() {
        return planDate;
    }

    public void setPlanDate(String planDate) {
        this.planDate = planDate;
    }
}

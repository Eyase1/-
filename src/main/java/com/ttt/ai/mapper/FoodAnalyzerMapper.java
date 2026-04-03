package com.ttt.ai.mapper;

import com.ttt.resp.ai.FoodAnalyzerResp;

import java.math.BigInteger;

public interface FoodAnalyzerMapper {
    FoodAnalyzerResp getFoodInformationByName(String foodname);

    void saveDishes(FoodAnalyzerResp foodAnalyzerResp);


    BigInteger getCategoryIdByName(String category);

    void saveCateGory(FoodAnalyzerResp foodAnalyzerResp);
}

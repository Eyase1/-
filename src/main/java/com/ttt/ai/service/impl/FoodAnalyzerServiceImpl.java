package com.ttt.ai.service.impl;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttt.ai.mapper.FoodAnalyzerMapper;
import com.ttt.ai.service.FoodAnalyzerService;
import com.ttt.req.ai.FoodAnalyzerReq;
import com.ttt.resp.ai.FoodAnalyzerResp;
import com.ttt.resp.ai.FoodPlanResp;
import com.ttt.util.ai.ChatUtil;
import com.ttt.util.result.Result;
import com.ttt.util.url.BaseUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collections;

@Service
public class FoodAnalyzerServiceImpl implements FoodAnalyzerService {
    @Autowired
    FoodAnalyzerMapper foodAnalyzerMapper;

    @Override
    public Result analyzeFood(FoodAnalyzerReq foodAnalyzerReq) throws Exception {
        FoodAnalyzerResp foodAnalyzerResp=new FoodAnalyzerResp();
        if(foodAnalyzerReq.getFoodname()!=null&&!foodAnalyzerReq.getFoodname().equals("null")&&!foodAnalyzerReq.getFoodname().equals("")){
             foodAnalyzerResp = foodAnalyzerMapper.getFoodInformationByName(foodAnalyzerReq.getFoodname());
            if (foodAnalyzerResp != null) {
                return Result.ok(foodAnalyzerResp);
            }
        }
        String uri=foodAnalyzerReq.getUrl();
        if(foodAnalyzerReq.getUrl()!=null&&!foodAnalyzerReq.getUrl().equals("null")){
            foodAnalyzerReq.setUrl(BaseUrl.baseUrl+foodAnalyzerReq.getUrl());
        }

        String content= ChatUtil.getInformationByAi(foodAnalyzerReq);
        ObjectMapper mapper = new ObjectMapper();
        try{
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }catch (Exception e){
            return Result.build(null,400,"出错，请稍后重试"+e.getMessage());
        }

        foodAnalyzerResp=mapper.readValue(content, FoodAnalyzerResp.class);
        if(foodAnalyzerReq.getUrl()!=null&&!foodAnalyzerReq.getUrl().equals("null")){
            foodAnalyzerResp.setImageUrl(uri);
        }
        BigInteger categoryId=foodAnalyzerMapper.getCategoryIdByName(foodAnalyzerResp.getCategory());
        if(categoryId==null){
            foodAnalyzerMapper.saveCateGory(foodAnalyzerResp);
            categoryId=foodAnalyzerMapper.getCategoryIdByName(foodAnalyzerResp.getCategory());

        }

        FoodAnalyzerResp data=foodAnalyzerMapper.getFoodInformationByName(foodAnalyzerResp.getName());
        if(data==null){
            foodAnalyzerResp.setCategory_id(categoryId);
            foodAnalyzerMapper.saveDishes(foodAnalyzerResp);
        }


        return Result.ok(foodAnalyzerResp);
    }

    @Override
    public Result getPlan(FoodAnalyzerReq foodAnalyzerReq) throws Exception {
        foodAnalyzerReq.setDatabaseUrl(BaseUrl.databaseUrl);
        String content=ChatUtil.getInformationByAi(foodAnalyzerReq);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        FoodPlanResp foodPlanResp=new FoodPlanResp();
        try {
             foodPlanResp=mapper.readValue(content, FoodPlanResp.class);
        }catch (Exception e){
            return Result.build(null,400,"当前出现异常请稍后重试"+e.getMessage());
        }

        return Result.ok(foodPlanResp);
    }
}
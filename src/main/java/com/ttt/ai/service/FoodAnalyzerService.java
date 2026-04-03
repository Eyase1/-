package com.ttt.ai.service;

import com.ttt.req.ai.FoodAnalyzerReq;
import com.ttt.util.result.Result;

public interface FoodAnalyzerService {

    Result analyzeFood(FoodAnalyzerReq foodAnalyzerReq) throws Exception;

    Result getPlan(FoodAnalyzerReq foodAnalyzerReq) throws Exception;
}

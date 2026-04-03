package com.ttt.ai.controller;

import com.ttt.ai.service.FoodAnalyzerService;
import com.ttt.req.ai.FoodAnalyzerReq;
import com.ttt.util.result.Result;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class FoodAnalyzerController {

    private final FoodAnalyzerService service;

    public FoodAnalyzerController(FoodAnalyzerService service) {
        this.service = service;
    }
    @PostMapping("/analyze")
    public Result analyzeFood(@RequestBody FoodAnalyzerReq foodAnalyzerReq) throws Exception {
        return service.analyzeFood(foodAnalyzerReq);
    }

    @PostMapping("/getPlan")
    public Result getPlan(@RequestBody FoodAnalyzerReq foodAnalyzerReq) throws Exception {
        return service.getPlan(foodAnalyzerReq);
    }

}
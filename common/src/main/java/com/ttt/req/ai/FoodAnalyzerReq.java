package com.ttt.req.ai;

import lombok.Data;

import java.io.Serializable;

@Data
public class FoodAnalyzerReq implements Serializable {
    private String foodname;
    private String url;
    private String userid;
    private String databaseUrl;
}

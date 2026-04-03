package com.ttt.controller;

import com.ttt.service.TestaService;
import com.ttt.test.TestA;
import com.ttt.util.ai.ChatCompletionsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestAController {
    @Autowired
    TestaService testaService;
    @GetMapping("/test")
    public List<TestA> getTesta(){
        List<Object>cnt=ChatCompletionsUtil.chatCompletion("你好");

        return testaService.getTesta();
    }
}

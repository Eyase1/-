package com.ttt.controller;

import com.ttt.service.TestbService;
import com.ttt.test.TestA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestbController {
    @Autowired
    TestbService testbService;
    @PostMapping("/testt")
    public List<TestA>getTesta(){
        return testbService.getTesta();
    }
}

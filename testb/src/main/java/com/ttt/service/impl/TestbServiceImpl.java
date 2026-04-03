package com.ttt.service.impl;

import com.ttt.feign.TestaFeign;
import com.ttt.service.TestbService;
import com.ttt.test.TestA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestbServiceImpl implements TestbService {
    @Autowired
    TestaFeign testaFeign;
    @Override
    public List<TestA> getTesta() {
        return testaFeign.getTesta();
    }
}

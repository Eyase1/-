package com.ttt.service.impl;

import com.ttt.mapper.TestaMapper;
import com.ttt.service.TestaService;
import com.ttt.test.TestA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestaServiceImpl implements TestaService {
    @Autowired
    private TestaMapper testaMapper;

    @Override
    public List<TestA> getTesta() {
        return testaMapper.getTesta();
    }
}

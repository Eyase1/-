package com.ttt.feign;

import com.ttt.test.TestA;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "testa")
public interface TestaFeign {
    @GetMapping("/test")
    public List<TestA> getTesta();
}

package com.ttt.dishes.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user")
public interface IsAdminFeign {
    @PostMapping("/isAdmin")
    public boolean isAdmin(@RequestBody String token);
}

package com.graduatedesign.knowledgeservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "coze-proxy-service", path = "/api/coze")
public interface CozeServiceClient {

    @PostMapping(value = "/document/qa",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    String documentQa(@RequestBody String request);
}
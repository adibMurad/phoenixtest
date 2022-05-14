package com.example.phoenixtest.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "${feign.question.name}",
        url = "${feign.question.url}"
)
public interface QuestionsFeignClient {
    @GetMapping(headers = "Accept-Encoding: gzip")
    byte[] getQuestions(
            @RequestParam("page") int pageNumber,
            @RequestParam("pagesize") int pageSize,
            @RequestParam("sort") String sortField,
            @RequestParam("order") String sortOrder
    );
}

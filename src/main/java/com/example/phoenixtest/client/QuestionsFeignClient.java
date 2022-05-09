package com.example.phoenixtest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "questions",
        url = "https://api.stackexchange.com/2.3/questions/featured?site=stackoverflow"
)
public interface QuestionsFeignClient {
    //page=1&pagesize=20&order=desc&sort=creation&site=stackoverflow

    @GetMapping(headers = "Accept-Encoding: gzip")
    byte[] getQuestions(
            @RequestParam("page") int pageNumber,
            @RequestParam("pagesize") int pageSize,
            @RequestParam("sort") String sortField,
            @RequestParam("order") String sortOrder
    );
}

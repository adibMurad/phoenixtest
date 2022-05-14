package com.example.phoenixtest.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "${feign.user-details.name}",
        url = "${feign.user-details.url}"
)
public interface UserDetailsFeignClient {
    @GetMapping(path = "/{id}", headers = "Accept-Encoding: gzip")
    byte[] getUser(
            @PathVariable("id") int id,
            @RequestParam(value = "site") String site
    );
}

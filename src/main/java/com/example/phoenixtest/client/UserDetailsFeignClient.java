package com.example.phoenixtest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "users",
        url = "https://api.stackexchange.com/2.3/users"
)
public interface UserDetailsFeignClient {
    @GetMapping(path = "/{id}", headers = "Accept-Encoding: gzip")
    byte[] getUser(
            @PathVariable("id") int id,
            @RequestParam(value = "site", defaultValue = "stackoverflow") String site
    );
}

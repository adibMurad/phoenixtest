package com.example.phoenixtest.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder()
@Jacksonized
public class UserDetailsFeignResponse {
    List<UserDetails> items;

    @Value
    @Builder()
    @Jacksonized
    public static class UserDetails {
        @JsonProperty("user_id")
        Integer userId;
        @JsonProperty("creation_date")
        Long creationDate;
        @JsonProperty("display_name")
        String displayName;
    }
}

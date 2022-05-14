package com.example.phoenixtest.feign.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder()
@Jacksonized
public class UserDetailsFeignResponse {
    List<UserDetails> items;

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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

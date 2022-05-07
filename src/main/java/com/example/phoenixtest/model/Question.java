package com.example.phoenixtest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder()
public class Question {
    Integer id;
    @Singular
    List<String> tags;
    @JsonProperty("is_answered")
    @Builder.Default
    Boolean answered = false;
    @JsonProperty("view_count")
    Integer viewCount;
    @JsonProperty("answer_count")
    Integer answerCount;
    @JsonProperty("creation_date")
    LocalDateTime creationDate;
    @JsonProperty("user_id")
    Integer userId;
}

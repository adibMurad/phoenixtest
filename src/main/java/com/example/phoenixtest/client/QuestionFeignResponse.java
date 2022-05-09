package com.example.phoenixtest.client;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class QuestionFeignResponse {
    List<Question> items;

    @Value
    @Builder()
    @Jacksonized
    public static class Question {
        @JsonProperty("question_id")
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
        Long creationDate;
        Owner owner;
    }

    @Value
    @Builder()
    @Jacksonized
    public static class Owner {
        @JsonProperty("user_id")
        Integer userId;
    }
}

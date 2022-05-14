package com.example.phoenixtest.feign.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Builder
@Jacksonized
public class QuestionFeignResponse {
    List<Question> items;

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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

    @Getter
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    @Builder()
    @Jacksonized
    public static class Owner {
        @JsonProperty("user_id")
        Integer userId;
    }
}

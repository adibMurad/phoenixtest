package com.example.phoenixtest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Builder()
@Jacksonized
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Question {
    @EqualsAndHashCode.Include
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime creationDate;
    @JsonProperty("user_id")
    Integer userId;
}

package com.example.phoenixtest.service;

import com.example.phoenixtest.client.QuestionFeignResponse;
import com.example.phoenixtest.client.QuestionsFeignClient;
import com.example.phoenixtest.entity.QuestionEntity;
import com.example.phoenixtest.entity.TagEntity;
import com.example.phoenixtest.model.SortOrder;
import com.example.phoenixtest.repository.QuestionRepository;
import com.example.phoenixtest.repository.TagRepository;
import com.example.phoenixtest.util.JsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StartupService {
    @Value("${startup.question.count}")
    private int questionCount;
    private final QuestionsFeignClient feignClient;
    private final ObjectMapper mapper;
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void loadLatestQuestions() throws IOException {
        byte[] bytes = feignClient.getQuestions(1, questionCount, "creation", SortOrder.DESCENDING.toString());
        QuestionFeignResponse feignResponse = mapper.readValue(JsonUtil.toString(bytes), QuestionFeignResponse.class);
        final Set<TagEntity> tags = tagsFromFeignResponseQuestions(feignResponse.getItems());
        tagRepository.saveAll(tags);
        final Map<String, Integer> tagMap = tags.stream().collect(Collectors.toMap(TagEntity::getTag, TagEntity::getId));
        List<QuestionEntity> questions =
                feignResponse.getItems()
                        .stream()
                        .map(question -> StartupService.fromFeignResponseQuestion(question, tagMap))
                        .collect(Collectors.toList());
        questionRepository.saveAll(questions);
    }

    private static Set<TagEntity> tagsFromFeignResponseQuestions(List<QuestionFeignResponse.Question> questions) {
        return questions.stream()
                .flatMap(question -> question.getTags().stream()
                        .map(tag -> TagEntity.builder()
                                .tag(tag)
                                .build()))
                .collect(Collectors.toSet());
    }

    private static QuestionEntity fromFeignResponseQuestion(QuestionFeignResponse.Question question, Map<String, Integer> tagMap) {
        return QuestionEntity.builder()
                .id(question.getId())
                .tags(question.getTags().stream().map(tag -> TagEntity.builder().id(tagMap.get(tag)).tag(tag).build()).collect(Collectors.toList()))
                .answered(question.getAnswered())
                .viewCount(question.getViewCount())
                .answerCount(question.getAnswerCount())
                .creationDate(
                        Optional.ofNullable(question.getCreationDate())
                                .map(time -> LocalDateTime.ofInstant(Instant.ofEpochSecond(time), TimeZone.getTimeZone("UTC").toZoneId()))
                                .orElse(null)
                )
                .userId(Optional.ofNullable(question.getOwner()).map(QuestionFeignResponse.Owner::getUserId).orElse(null))
                .build();
    }

}

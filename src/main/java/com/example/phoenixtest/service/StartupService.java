package com.example.phoenixtest.service;

import com.example.phoenixtest.db.entity.QuestionEntity;
import com.example.phoenixtest.db.entity.TagEntity;
import com.example.phoenixtest.db.repository.QuestionRepository;
import com.example.phoenixtest.db.repository.TagRepository;
import com.example.phoenixtest.feign.client.QuestionsFeignClient;
import com.example.phoenixtest.feign.model.QuestionFeignResponse;
import com.example.phoenixtest.model.SortOrder;
import com.example.phoenixtest.util.CompressUtil;
import com.example.phoenixtest.util.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

    /**
     * Loads 20 newest (by date) featured questions from StackOverflow.com API.
     * The result of the call is stored into the database, replacing results from previous runs.
     *
     * @throws IOException if an error occurs processing the response JSON.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadLatestQuestions() throws IOException {
        log.info("Loading {} questions on application startup.", questionCount);
        // Get and uncompress data from Stack Overflow service:
        byte[] bytes = feignClient.getQuestions(1, questionCount, "creation", SortOrder.DESCENDING.toString());
        QuestionFeignResponse feignResponse = mapper.readValue(CompressUtil.decompress(bytes), QuestionFeignResponse.class);
        log.info("{} questions retrieved from service.", feignResponse.getItems().size());

        // Extract and persist the set of tags from Stack Overflow response:
        final Set<TagEntity> tags = tagsFromFeignResponseQuestions(feignResponse.getItems());
        tagRepository.saveAll(tags);
        log.info("{} tags persisted in database.", tags.size());

        // Persist the questions and their references to the tags:
        final Map<String, Integer> tagMap = tags.stream().collect(Collectors.toMap(TagEntity::getTag, TagEntity::getId));
        List<QuestionEntity> questions =
                feignResponse.getItems()
                        .stream()
                        .map(question -> StartupService.fromFeignResponseQuestion(question, tagMap))
                        .collect(Collectors.toList());
        questionRepository.saveAll(questions);
        log.info("{} questions persisted in database.", questions.size());
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
                .creationDate(DateUtil.fromEpochSecond(question.getCreationDate()))
                .userId(Optional.ofNullable(question.getOwner()).map(QuestionFeignResponse.Owner::getUserId).orElse(null))
                .build();
    }

}

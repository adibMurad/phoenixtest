package com.example.phoenixtest.service;

import com.example.phoenixtest.client.QuestionFeignResponse;
import com.example.phoenixtest.client.QuestionsFeignClient;
import com.example.phoenixtest.entity.QuestionEntity;
import com.example.phoenixtest.entity.TagEntity;
import com.example.phoenixtest.model.SortOrder;
import com.example.phoenixtest.repository.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class QuestionService {
    private final QuestionsFeignClient feignClient;
    private final ObjectMapper mapper;
    private final QuestionRepository repository;

    private String unzip(byte[] gzippedJSON) {
        try (InputStream byteStream = new ByteArrayInputStream(gzippedJSON);
             GZIPInputStream gzipStream = new GZIPInputStream(byteStream)) {
            return new String(gzipStream.readAllBytes());
        } catch (IOException e) {
            log.error("Error decompressing JSON.", e);
            throw new ServerErrorException("Error decompressing JSON.", e);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadLatestQuestions() throws IOException {
        byte[] bytes = feignClient.getQuestions(1, 20, "creation", SortOrder.DESCENDING.toString());
        QuestionFeignResponse feignResponse = mapper.readValue(unzip(bytes), QuestionFeignResponse.class);
        List<QuestionEntity> questions =
                feignResponse.getItems()
                        .stream()
                        .map(QuestionService::fromFeignResponseQuestion)
                        .collect(Collectors.toList());
        questions.forEach(question -> {
            if (question.getTags().stream().anyMatch(t -> t.getTag() == null)) System.out.println(question);
        });
        repository.saveAll(questions);
    }

    private static QuestionEntity fromFeignResponseQuestion(QuestionFeignResponse.Question question) {
        return QuestionEntity.builder()
                .id(question.getId())
                .tags(question.getTags().stream().map(tag -> TagEntity.builder().tag(tag).build()).collect(Collectors.toList()))
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

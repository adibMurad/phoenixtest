package com.example.phoenixtest.service;

import com.example.phoenixtest.db.entity.QuestionEntity;
import com.example.phoenixtest.db.entity.TagEntity;
import com.example.phoenixtest.db.repository.QuestionRepository;
import com.example.phoenixtest.db.repository.TagRepository;
import com.example.phoenixtest.model.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;

    public List<Question> findAll() {
        final List<Question> questions = questionRepository.findAll().stream()
                .map(QuestionService::fromQuestionEntity)
                .collect(Collectors.toList());
        log.debug("findAll: Found {} questions", questions.size());
        return questions;
    }

    public List<Question> findAll(List<String> tags) {
        final List<Question> questions = tagRepository.findAllByTags(tags).stream()
                .flatMap(tagEntity -> tagEntity.getQuestions().stream().map(QuestionService::fromQuestionEntity))
                .distinct()
                .sorted(Comparator.comparing(Question::getCreationDate).reversed())
                .collect(Collectors.toList());
        log.debug("findAll({}): Found {} questions", tags, questions.size());
        return questions;
    }

    public Question findById(Integer id) {
        final Question question = questionRepository.findById(id).map(QuestionService::fromQuestionEntity).orElse(null);
        log.debug("findById({}): Question {}found", id, question == null ? "not " : "");
        return question;
    }

    public boolean delete(Integer id) {
        if (findById(id) == null) {
            return false;
        }
        log.debug("delete({}): Question deleted", id);
        questionRepository.deleteById(id);
        return true;
    }

    static Question fromQuestionEntity(QuestionEntity questionEntity) {
        return Question.builder()
                .id(questionEntity.getId())
                .tags(Optional.ofNullable(questionEntity.getTags())
                        .map(tags -> questionEntity.getTags().stream().map(TagEntity::getTag).collect(Collectors.toList()))
                        .orElse(Collections.emptyList()))
                .answered(questionEntity.getAnswered())
                .viewCount(questionEntity.getViewCount())
                .answerCount(questionEntity.getAnswerCount())
                .creationDate(questionEntity.getCreationDate())
                .userId(questionEntity.getUserId())
                .build();
    }


}

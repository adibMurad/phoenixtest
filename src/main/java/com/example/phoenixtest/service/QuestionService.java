package com.example.phoenixtest.service;

import com.example.phoenixtest.entity.QuestionEntity;
import com.example.phoenixtest.entity.TagEntity;
import com.example.phoenixtest.model.Question;
import com.example.phoenixtest.repository.QuestionRepository;
import com.example.phoenixtest.repository.TagRepository;
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
        return questionRepository.findAll().stream()
                .map(QuestionService::fromQuestionEntity)
                .collect(Collectors.toList());
    }

    public List<Question> findAll(List<String> tags) {
        return tagRepository.findAllByTags(tags).stream()
                .flatMap(tagEntity -> tagEntity.getQuestions().stream().map(QuestionService::fromQuestionEntity))
                .distinct()
                .sorted(Comparator.comparing(Question::getCreationDate).reversed())
                .collect(Collectors.toList());
    }

    public Question findById(Integer id) {
        return questionRepository.findById(id).map(QuestionService::fromQuestionEntity).orElse(null);
    }

    public Question delete(Integer id) {
        Question question = findById(id);
        if (question == null) {
            return null;
        }
        questionRepository.deleteById(id);
        return question;
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

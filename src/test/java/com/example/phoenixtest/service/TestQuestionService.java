package com.example.phoenixtest.service;

import com.example.phoenixtest.db.entity.QuestionEntity;
import com.example.phoenixtest.db.entity.TagEntity;
import com.example.phoenixtest.db.repository.QuestionRepository;
import com.example.phoenixtest.db.repository.TagRepository;
import com.example.phoenixtest.model.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("Question Service test")
class TestQuestionService {
    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private QuestionService questionService;

    private QuestionEntity testEntity;

    @BeforeEach
    public void init() {
        final List<String> testTags = Arrays.asList("tag1", "tag2", "tag3");
        testEntity = QuestionEntity.builder()
                .id(1)
                .tags(testTags.stream()
                        .map(tag -> TagEntity.builder().tag(tag).build())
                        .collect(Collectors.toList()))
                .answered(true)
                .viewCount(2)
                .answerCount(3)
                .creationDate(LocalDateTime.now())
                .userId(4)
                .build();
    }

    @Test
    public void testFindAll() {
        // GIVEN
        when(questionRepository.findAll()).thenReturn(Collections.singletonList(testEntity));

        // WHEN
        List<Question> actual = questionService.findAll();

        // THEN
        assertEquals(1, actual.size());
        assertEqualsQuestion(actual.get(0));
    }

    @DisplayName("Test get by tags")
    @Test
    public void testGetByTags() {
        // GIVEN
        final LocalDateTime today = LocalDateTime.now();
        final LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        final LocalDateTime back2days = LocalDateTime.now().minusDays(2);
        List<QuestionEntity> testQuestionEntities = Arrays.asList(
                QuestionEntity.builder().id(1).creationDate(yesterday).build(),
                QuestionEntity.builder().id(2).creationDate(back2days).build(),
                QuestionEntity.builder().id(3).creationDate(today).build()
        );
        final String testTag = "tag";
        final TagEntity tagEntity = TagEntity.builder()
                .id(1)
                .tag(testTag)
                .questions(testQuestionEntities)
                .build();

        when(tagRepository.findAllByTags(Collections.singletonList(testTag)))
                .thenReturn(Collections.singletonList(tagEntity));

        // WHEN
        List<Question> actualFound = questionService.findAll(Collections.singletonList(testTag));
        List<Question> actualNotFound = questionService.findAll(Collections.singletonList("other"));

        // THEN
        verify(tagRepository, times(1)).findAllByTags(Collections.singletonList(testTag));

        assertNotNull(actualFound);
        assertEquals(3, actualFound.size());
        assertNotNull(actualNotFound);
        assertEquals(0, actualNotFound.size());

        assertEquals(today, actualFound.get(0).getCreationDate());
        assertEquals(yesterday, actualFound.get(1).getCreationDate());
        assertEquals(back2days, actualFound.get(2).getCreationDate());
    }

    @Test
    public void testFindById() {
        // GIVEN
        final int idFound = 1;
        when(questionRepository.findById(idFound)).thenReturn(Optional.of(testEntity));

        // WHEN
        Question actualFound = questionService.findById(idFound);
        Question actualNotFound = questionService.findById(2);

        // THEN
        assertNull(actualNotFound);
        assertNotNull(actualFound);
        assertEqualsQuestion(actualFound);
    }

    @Test
    public void testDelete() {
        // GIVEN
        final int idFound = 1;
        when(questionRepository.findById(idFound)).thenReturn(Optional.of(testEntity));

        // WHEN
        boolean actualFound = questionService.delete(idFound);
        boolean actualNotFound = questionService.delete(2);

        // THEN
        verify(questionRepository, times(1)).deleteById(anyInt());
        assertFalse(actualNotFound);
        assertTrue(actualFound);
    }

    private void assertEqualsQuestion(Question actual) {
        assertEquals(testEntity.getId(), actual.getId());
        assertTrue(testEntity.getTags().stream().map(TagEntity::getTag).collect(Collectors.toList()).containsAll(actual.getTags()));
        assertTrue(actual.getTags().containsAll(testEntity.getTags().stream().map(TagEntity::getTag).collect(Collectors.toList())));
        assertEquals(testEntity.getAnswered(), actual.getAnswered());
        assertEquals(testEntity.getViewCount(), actual.getViewCount());
        assertEquals(testEntity.getAnswerCount(), actual.getAnswerCount());
        assertEquals(testEntity.getCreationDate(), actual.getCreationDate());
        assertEquals(testEntity.getUserId(), actual.getUserId());
    }

}

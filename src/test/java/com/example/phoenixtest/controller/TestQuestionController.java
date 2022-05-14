package com.example.phoenixtest.controller;

import com.example.phoenixtest.model.Question;
import com.example.phoenixtest.service.QuestionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("Question Controller test")
class TestQuestionController {
    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    private static Stream<Arguments> testGetByTagsProvider() {
        return Stream.of(
                Pair.of(Collections.emptyList(), Collections.emptyList()),
                Pair.of(Collections.singletonList("tag"), Collections.emptyList()),
                Pair.of(Collections.singletonList("tag"), Collections.singletonList("")),
                Pair.of(Collections.singletonList("tag"), Collections.singletonList("tag")),
                Pair.of(Arrays.asList("tag1", "tag2", "tag3"), Arrays.asList("tag2", "")),
                Pair.of(Arrays.asList("tag1", "tag2", "tag3"), Collections.singletonList("tag4")),
                Pair.of(Arrays.asList("tag1", "tag2", "tag3"), Arrays.asList("tag4", ""))
        ).map(pair -> Arguments.of(pair.getFirst(), pair.getSecond()));
    }

    @DisplayName("Test get by tags")
    @ParameterizedTest(name = "#{index} - Existing tags: {0} | Search tags: {1}")
    @MethodSource("testGetByTagsProvider")
    public void testGetByTags(List<String> existingTags, List<String> searchTags) {
        // GIVEN
        final boolean getAllQuestions = CollectionUtils.isEmpty(searchTags);
        final boolean anyTagFound = !getAllQuestions && searchTags.stream().anyMatch(existingTags::contains);
        final Question testQuestion = Question.builder().build();

        lenient().when(questionService.findAll())
                .thenReturn(Collections.singletonList(testQuestion));
        lenient().when(questionService.findAll(searchTags))
                .thenReturn(anyTagFound
                        ? Collections.singletonList(testQuestion)
                        : Collections.emptyList());

        // WHEN
        ResponseEntity<List<Question>> actual = questionController.getByTags(searchTags);

        // THEN
        verify(questionService, times(getAllQuestions ? 1 : 0)).findAll();
        verify(questionService, times(getAllQuestions ? 0 : 1)).findAll(anyList());
        if (getAllQuestions || anyTagFound) {
            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertNotNull(actual.getBody());
            assertEquals(1, actual.getBody().size());
        } else {
            assertEquals(HttpStatus.NO_CONTENT, actual.getStatusCode());
            assertNull(actual.getBody());
        }
    }

    @DisplayName("Test get by id")
    @Test
    public void testGetById() {
        // GIVEN
        final int idExists = 1;
        final int idNotFound = 2;
        final Question testQuestion = Question.builder().build();

        when(questionService.findById(idExists)).thenReturn(testQuestion);
        when(questionService.findById(idNotFound)).thenReturn(null);

        // WHEN
        ResponseEntity<Question> actualExisting = questionController.getById(idExists);
        ResponseEntity<Question> actualNotFound = questionController.getById(idNotFound);

        // THEN
        verify(questionService, times(2)).findById(anyInt());
        assertEquals(HttpStatus.OK, actualExisting.getStatusCode());
        assertNotNull(actualExisting.getBody());
        assertEquals(HttpStatus.NOT_FOUND, actualNotFound.getStatusCode());
        assertNull(actualNotFound.getBody());
    }

    @DisplayName("Test delete by id")
    @Test
    public void testDeleteById() {
        // GIVEN
        final Integer idExists = 1;
        final Integer idNotFound = 2;

        when(questionService.delete(idExists)).thenReturn(true);

        // WHEN
        ResponseEntity<Void> actualExisting = questionController.deleteById(idExists);
        ResponseEntity<Void> actualNotFound = questionController.deleteById(idNotFound);

        // THEN
        verify(questionService, times(2)).delete(anyInt());
        assertEquals(HttpStatus.OK, actualExisting.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, actualNotFound.getStatusCode());
    }

}

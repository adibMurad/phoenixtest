package com.example.phoenixtest.controller;

import com.example.phoenixtest.model.Question;
import com.example.phoenixtest.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/question")
@Tag(name = "question-controller", description = "API to query questions from StackOverflow.")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class QuestionController {
    private final QuestionService questionService;

    @ApiOperation(
            value = "Retrieves questions with given tags.",
            notes = "Retrieves questions that have any of the specified tags or all of them if no tag was specified.",
            tags = {"question-controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of questions from newer to older."),
            @ApiResponse(code = 204, message = "Questions database is empty or no questions found with the given tags."),
            @ApiResponse(code = 500, message = "An unexpected error has occurred. The error has been logged and is being investigated.")})
    @GetMapping
    public ResponseEntity<List<Question>> getByTags(@RequestParam(required = false) List<String> tag) {
        List<Question> questions = CollectionUtils.isEmpty(tag) ? questionService.findAll() : questionService.findAll(tag);
        return CollectionUtils.isEmpty(questions)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(questions);
    }

    @ApiOperation(
            value = "Retrieves a question by its id.",
            notes = "Retrieves the question with specified id.",
            tags = {"question-controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question data."),
            @ApiResponse(code = 404, message = "Question id wasn't found."),
            @ApiResponse(code = 500, message = "An unexpected error has occurred. The error has been logged and is being investigated.")})
    @GetMapping("/{id}")
    public ResponseEntity<Question> getById(@PathVariable Integer id) {
        Question question = questionService.findById(id);
        return question == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(question);
    }

    @ApiOperation(
            value = "Deletes the question with specified id.",
            notes = "Deletes the question with specified id.",
            tags = {"question-controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question successfully deleted."),
            @ApiResponse(code = 404, message = "Question id wasn't found."),
            @ApiResponse(code = 500, message = "An unexpected error has occurred. The error has been logged and is being investigated.")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        return questionService.delete(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }

}

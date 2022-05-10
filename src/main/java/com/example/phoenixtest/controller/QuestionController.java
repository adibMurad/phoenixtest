package com.example.phoenixtest.controller;

import com.example.phoenixtest.model.Question;
import com.example.phoenixtest.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;

@RestController
@RequestMapping(path = "/question", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "question-controller", description = "API to query questions from StackOverflow.")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class QuestionController {
    private final QuestionService questionService;

    @ApiOperation(
            value = "Retrieves all questions, filtering by tags.",
            notes = "Retrieves all questions that have the specified tags or all of them if no tag was specified.",
            tags = {"question-controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of questions from newer to older."),
            @ApiResponse(code = 204, message = "Questions database is empty or no questions found with the given tags."),
            @ApiResponse(code = 500, message = "An unexpected error has occurred. The error has been logged and is being investigated.")})
    @GET
    public Response getByTags(@RequestParam List<String> tags) {
        List<Question> questions = CollectionUtils.isEmpty(tags) ? questionService.findAll() : questionService.findAll(tags);
        return CollectionUtils.isEmpty(questions)
                ? Response.noContent().build()
                : Response.ok(questions).build();
    }

    @ApiOperation(
            value = "Retrieves a question by its id.",
            notes = "Retrieves the question with specified id.",
            tags = {"question-controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question data."),
            @ApiResponse(code = 204, message = "Question id wasn't found."),
            @ApiResponse(code = 500, message = "An unexpected error has occurred. The error has been logged and is being investigated.")})
    @GET
    @Path("/{id}")
    public Response getById(@PathVariable Integer id) {
        Question question = questionService.findById(id);
        return question == null
                ? Response.noContent().build()
                : Response.ok(question).build();
    }

    @ApiOperation(
            value = "Deletes the question with specified id.",
            notes = "Deletes the question with specified id.",
            tags = {"question-controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Question successfully deleted."),
            @ApiResponse(code = 204, message = "Question id wasn't found."),
            @ApiResponse(code = 500, message = "An unexpected error has occurred. The error has been logged and is being investigated.")})
    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathVariable Integer id) {
        Question question = questionService.delete(id);
        return question == null
                ? Response.noContent().build()
                : Response.ok().build();
    }

}

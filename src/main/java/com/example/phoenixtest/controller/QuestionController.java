package com.example.phoenixtest.controller;

import com.example.phoenixtest.model.Question;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/question", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "question-controller", description = "API to query questions from StackOverflow.")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class QuestionController {
    @ApiOperation(
            value = "Retrieves all questions, filtering by tags.",
            notes = "Retrieves all questions that have the specified tags or all of them if no tag was specified.",
            tags = {"question-controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of questions from newer to older."),
            @ApiResponse(code = 204, message = "Questions database is empty."),
            @ApiResponse(code = 500, message = "An unexpected error has occurred. The error has been logged and is being investigated.")})
    @GET
    public List<Question> getByTags(@RequestParam List<String> tags) {
        return Collections.emptyList();
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
    public Question getById(@PathVariable Integer id) {
        return null;
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
        return Response.ok().build();
    }

}

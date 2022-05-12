package com.example.phoenixtest.controller;

import com.example.phoenixtest.model.UserDetails;
import com.example.phoenixtest.service.UserDetailsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/user")
@Tag(name = "user-controller", description = "API to query user data from StackOverflow.")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDetailsController {
    private final UserDetailsService userService;

    @ApiOperation(
            value = "Retrieve user details.",
            notes = "Retrieve details about a user with a given id.",
            tags = {"user-controller"}
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User details."),
            @ApiResponse(code = 204, message = "User id wasn't found."),
            @ApiResponse(code = 500, message = "An unexpected error has occurred. The error has been logged and is being investigated.")})
    @GetMapping("/{id}")
    public ResponseEntity<UserDetails> getById(@PathVariable Integer id) {
        UserDetails userDetails = userService.findById(id);
        return userDetails == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(userDetails);
    }

}

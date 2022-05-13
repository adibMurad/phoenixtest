package com.example.phoenixtest.controller;

import com.example.phoenixtest.model.UserDetails;
import com.example.phoenixtest.service.UserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetails Controller test")
public class TestUserDetailsController {
    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private UserDetailsController userDetailsController;

    @Test
    public void getById() {
        // GIVEN
        final int idFound = 1;
        final UserDetails testUserDetails = UserDetails.of(1, LocalDateTime.now(), "name");
        when(userDetailsService.findById(idFound)).thenReturn(testUserDetails);

        // WHEN
        ResponseEntity<UserDetails> actualFound = userDetailsController.getById(idFound);
        ResponseEntity<UserDetails> actualNotFound = userDetailsController.getById(999);

        // THEN
        verify(userDetailsService, times(2)).findById(anyInt());
        assertEquals(HttpStatus.OK, actualFound.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, actualNotFound.getStatusCode());
        assertNull(actualNotFound.getBody());
        assertNotNull(actualFound.getBody());
        assertEquals(testUserDetails.getUserId(), actualFound.getBody().getUserId());
        assertEquals(testUserDetails.getCreationDate(), actualFound.getBody().getCreationDate());
        assertEquals(testUserDetails.getDisplayName(), actualFound.getBody().getDisplayName());
    }

}

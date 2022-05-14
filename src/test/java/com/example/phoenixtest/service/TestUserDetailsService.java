package com.example.phoenixtest.service;

import com.example.phoenixtest.feign.client.UserDetailsFeignClient;
import com.example.phoenixtest.feign.model.UserDetailsFeignResponse;
import com.example.phoenixtest.model.UserDetails;
import com.example.phoenixtest.util.CompressUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ServerErrorException;

import java.time.ZoneOffset;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("UserDetails Service test")
public class TestUserDetailsService {
    @Mock
    private UserDetailsFeignClient feignClient;

    @Autowired
    private ObjectMapper mapper;

    private UserDetailsService userDetailsService;

    @BeforeEach
    public void init() {
        userDetailsService = spy(new UserDetailsService(feignClient, mapper));
    }

    @Test
    public void testFindById() throws JsonProcessingException {
        // GIVEN
        final int idFound = 1;
        final int idNotFound = 2;
        final int idError = 999;
        final UserDetailsFeignResponse.UserDetails expected = UserDetailsFeignResponse.UserDetails.builder()
                .userId(1)
                .creationDate(2L)
                .displayName("name")
                .build();
        final UserDetailsFeignResponse feignResponseFound = UserDetailsFeignResponse.builder()
                .items(Collections.singletonList(expected))
                .build();
        final UserDetailsFeignResponse feignResponseNotFound = UserDetailsFeignResponse.builder().build();
        final String feignResponseFoundJson = mapper.writeValueAsString(feignResponseFound);
        final String feignResponseNotFoundJson = mapper.writeValueAsString(feignResponseNotFound);
        when(feignClient.getUser(eq(idFound), any()))
                .thenReturn(CompressUtil.compress(feignResponseFoundJson));
        lenient().when(feignClient.getUser(eq(idNotFound), any()))
                .thenReturn(CompressUtil.compress(feignResponseNotFoundJson));
        lenient().when(feignClient.getUser(eq(idError), any()))
                .thenReturn(CompressUtil.compress("{garbage to cause an error" + feignResponseFoundJson));

        // WHEN
        UserDetails userDetailsFound = userDetailsService.findById(idFound);
        UserDetails userDetailsNotFound = userDetailsService.findById(idNotFound);

        // THEN
        assertNotNull(userDetailsFound);
        assertNull(userDetailsNotFound);
        assertEquals(expected.getUserId(), userDetailsFound.getUserId());
        assertEquals(expected.getCreationDate(), userDetailsFound.getCreationDate().toEpochSecond(ZoneOffset.UTC));
        assertEquals(expected.getDisplayName(), userDetailsFound.getDisplayName());

        assertThrows(ServerErrorException.class, () -> userDetailsService.findById(idError));
    }
}

package com.example.phoenixtest.feign.exception;

import com.example.phoenixtest.util.CompressUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerErrorException;

import java.io.IOException;

@Component
public class UserDetailsErrorDecoder implements ErrorDecoder {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public Exception decode(String methodKey, Response response) {
        ErrorDTO errorDTO;

        try {
            final String body = CompressUtil.decompress(response.body().asInputStream().readAllBytes());
            errorDTO = mapper.readValue(body, ErrorDTO.class);
        } catch (IOException e) {
            throw new ServerErrorException("Failed to decode error.", e);
        }

        return new UserDetailsFeignException(errorDTO.getId(), errorDTO.getName(), errorDTO.getMessage());
    }

    @Getter
    private static class ErrorDTO {
        @JsonProperty("error_id")
        private int id;
        @JsonProperty("error_name")
        private String name;
        @JsonProperty("error_message")
        private String message;
    }
}

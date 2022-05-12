package com.example.phoenixtest.service;

import com.example.phoenixtest.client.UserDetailsFeignClient;
import com.example.phoenixtest.client.UserDetailsFeignResponse;
import com.example.phoenixtest.model.UserDetails;
import com.example.phoenixtest.util.CompressUtil;
import com.example.phoenixtest.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDetailsService {
    private static final String PARAM_SITE = "stackoverflow";

    private final UserDetailsFeignClient feignClient;
    private final ObjectMapper mapper;

    @Cacheable(value = "userDetailsCache", key = "#id")
    public UserDetails findById(Integer id) {
        log.info("User details cache refreshed.");
        try {
            // Get and uncompress data from Stack Overflow service:
            byte[] bytes = feignClient.getUser(id, PARAM_SITE);
            UserDetailsFeignResponse response = mapper.readValue(CompressUtil.decompress(bytes), UserDetailsFeignResponse.class);

            // Map the service response to a UserDetails object:
            List<UserDetailsFeignResponse.UserDetails> items = Optional.ofNullable(response).map(UserDetailsFeignResponse::getItems).orElse(null);
            return Optional.ofNullable(items)
                    .flatMap(list -> list.stream().findFirst()
                            .map(UserDetailsService::fromFeignResponseUserDetails))
                    .orElse(null);
        } catch (JsonProcessingException e) {
            log.error("Error processing user details data.", e);
            throw new WebApplicationException(e);
        }
    }

    private static UserDetails fromFeignResponseUserDetails(UserDetailsFeignResponse.UserDetails userDetailsResponse) {
        return UserDetails.of(
                userDetailsResponse.getUserId(),
                DateUtil.fromEpochSecond(userDetailsResponse.getCreationDate()),
                userDetailsResponse.getDisplayName());
    }
}

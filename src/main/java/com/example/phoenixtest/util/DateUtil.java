package com.example.phoenixtest.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

@Slf4j
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {
    public static LocalDateTime fromEpochSecond(Long epochSecond) {
        return Optional.ofNullable(epochSecond)
                .map(time -> LocalDateTime.ofInstant(Instant.ofEpochSecond(time), TimeZone.getTimeZone("UTC").toZoneId()))
                .orElse(LocalDateTime.MIN);
    }
}

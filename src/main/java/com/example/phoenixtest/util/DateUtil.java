package com.example.phoenixtest.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {
    public static LocalDateTime fromEpochSecond(Long epochSecond) {
        return Optional.ofNullable(epochSecond)
                .map(time -> LocalDateTime.ofInstant(Instant.ofEpochSecond(time), TimeZone.getTimeZone("UTC").toZoneId()))
                .orElse(LocalDateTime.MIN);
    }
}
